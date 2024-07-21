package dev.borisochieng.malltiverse.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.remote.NetworkResponse
import dev.borisochieng.malltiverse.domain.LocalDatabaseRepository
import dev.borisochieng.malltiverse.domain.TimbuAPIRepository
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import dev.borisochieng.malltiverse.domain.toOrder
import dev.borisochieng.malltiverse.domain.toWishListItem
import dev.borisochieng.malltiverse.util.Constants.API_KEY
import dev.borisochieng.malltiverse.util.Constants.APP_ID
import dev.borisochieng.malltiverse.util.Constants.ORGANIZATION_ID
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider
import dev.borisochieng.malltiverse.util.UIEvents
import dev.borisochieng.malltiverse.util.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    private val timbuAPIRepository: TimbuAPIRepository,
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> get() = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvents>()
    val eventFlow: SharedFlow<UIEvents> get() = _eventFlow.asSharedFlow()

    init {
        getProducts(
            apiKey = API_KEY,
            organizationID = ORGANIZATION_ID,
            appId = APP_ID
        )
    }


    fun getProducts(
        apiKey: String,
        organizationID: String,
        appId: String
    ) =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            val productsResponse = timbuAPIRepository.getProducts(
                organizationID = organizationID,
                appID = appId,
                apiKey = apiKey
            )

            when (productsResponse) {
                is NetworkResponse.Success -> {
                    val allProducts = productsResponse.payLoad ?: emptyList()

                    withContext(dispatcher.Default) {
                        val groupedProductsByCategory = allProducts.flatMap { product ->
                            product.category.map { category ->
                                category.name to product
                            }
                        }.groupBy({ it.first }, { it.second })

                        withContext(dispatcher.Main) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    products = allProducts,
                                    categoriesWithProducts = groupedProductsByCategory
                                )
                            }
                        }
                    }
                }

                is NetworkResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = productsResponse.message
                        )
                    }

                    _eventFlow.emit(
                        UIEvents.SnackBarEvent(
                            message = productsResponse.message
                        )
                    )
                }
            }


        }

    fun getProduct(
        productId: String,
        apiKey: String,
        organizationID: String,
        appId: String
    ) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = ""
            )
        }
        val productResponse = timbuAPIRepository.getProduct(
            productID = productId,
            apiKey = apiKey,
            organizationID = organizationID,
            appID = appId
        )

        when (productResponse) {
            is NetworkResponse.Success -> {
                val product = productResponse.payLoad
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = product
                    )
                }
            }

            is NetworkResponse.Error -> {
                val errorMessage = productResponse.message
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
                _eventFlow.emit(
                    UIEvents.SnackBarEvent(
                        message = errorMessage
                    )
                )
            }
        }
    }

    fun toggleCart(product: DomainProduct) = viewModelScope.launch {
        val updatedCategories = _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
            products.map { p ->
                if (p.id == product.id) {
                    p.copy(
                        isAddedToCart = !p.isAddedToCart,
                        quantity = if (!p.isAddedToCart) 1 else 1
                    )
                } else {
                    p
                }
            }
        }

        _uiState.update {
            it.copy(categoriesWithProducts = updatedCategories)
        }

        val updatedCartItems = getCartItems(updatedCategories)

        _uiState.update {
            it.copy(cartItems = updatedCartItems)
        }

        Log.d("Toggle Cart", "toggleCart: ${product.name} ${product.isAddedToCart}")
    }

    fun updateQuantity(product: DomainProduct, newQuantity: Int) =
        viewModelScope.launch {
            val updatedCartItems =
                _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
                    products.map {
                        if (it.id == product.id) {
                            it.copy(
                                quantity = newQuantity,
                                price = newQuantity * it.price
                            )
                        } else {
                            it
                        }
                    }
                }

            _uiState.update {
                it.copy(categoriesWithProducts = updatedCartItems)
            }

            val updatedCartItemsList = getCartItems(updatedCartItems)
            _uiState.update {
                it.copy(cartItems = updatedCartItemsList)
            }
        }

    fun getTotalCartPrice(): Double =
        _uiState.value.cartItems.sumOf { it.price * it.quantity }

    fun addCartItemsToLocalDb() =
        viewModelScope.launch {
            val cartItems = _uiState.value.cartItems
            cartItems.forEach { item ->
                localDatabaseRepository.addOrder(item.toOrder())
            }
        }

    private fun getCartItems(categories: Map<String, List<DomainProduct>>): List<DomainProduct> =
        categories.values.flatten().filter { it.isAddedToCart }

    fun addToWishList(product: DomainWishlistItem) =
        viewModelScope.launch {
            val isProductAlreadyInWishlist = localDatabaseRepository.addToWishList(product.toWishListItem())

            when(isProductAlreadyInWishlist) {
                is DatabaseResponse.Success -> {
                    val updatedWishlistItems = _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
                        products.map { p ->
                            if (p.id == product.id) {
                                p.copy(
                                    isAddedToWishlist = !p.isAddedToWishlist,
                                )
                            } else {
                                p
                            }
                        }
                    }

                    _uiState.update {
                        it.copy(categoriesWithProducts = updatedWishlistItems)
                    }

                    _eventFlow.emit(
                        UIEvents.SnackBarEvent(
                            message = "Product added to wishlist"
                        )
                    )
                }
                is DatabaseResponse.Error -> {
                    _eventFlow.emit(
                        UIEvents.SnackBarEvent(
                            message = isProductAlreadyInWishlist.message
                        )
                    )
                }
            }
        }

    fun getWishlist() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            val wishlist = localDatabaseRepository.getWishlist()

            wishlist.collect { wishListResponse ->
                when (wishListResponse) {
                    is DatabaseResponse.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            wishlist = wishListResponse.items ?: emptyList()
                        )
                        _eventFlow.emit(
                            UIEvents.SnackBarEvent(
                                message = "Product added to wishlist"
                            )
                        )
                    }

                    is DatabaseResponse.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = wishListResponse.message
                            )
                        }

                        _eventFlow.emit(
                            UIEvents.SnackBarEvent(
                                message = wishListResponse.message
                            )
                        )

                    }
                }

            }
        }

    fun removeFromWishList(product: DomainWishlistItem) =
        viewModelScope.launch {
            val isProductInWishlist = localDatabaseRepository.removeFromWishlist(product.toWishListItem())

            when(isProductInWishlist) {
                is DatabaseResponse.Success -> {
                    _eventFlow.emit(
                        UIEvents.SnackBarEvent(
                            message = "Removed from wishlist"
                        )
                    )
                }
                is DatabaseResponse.Error -> {
                    _eventFlow.emit(
                        UIEvents.SnackBarEvent(
                            message = isProductInWishlist.message
                        )
                    )
                }
            }
        }
}