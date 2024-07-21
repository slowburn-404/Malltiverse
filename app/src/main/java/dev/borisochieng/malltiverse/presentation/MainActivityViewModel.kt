package dev.borisochieng.malltiverse.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.remote.NetworkResponse
import dev.borisochieng.malltiverse.domain.LocalDatabaseRepository
import dev.borisochieng.malltiverse.domain.TimbuAPIRepository
import dev.borisochieng.malltiverse.domain.models.DomainOrder
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

class MainActivityViewModel(
    private val timbuAPIRepository: TimbuAPIRepository,
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> get() = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvents>()
    val eventFlow: SharedFlow<UIEvents> get() = _eventFlow.asSharedFlow()

    private var cartItemsCache = listOf<DomainProduct>()

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
    ) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = ""
            )
        }

        withTimeoutOrNull(10000L) { // Timeout after 10 seconds
            val productsResponse = timbuAPIRepository.getProducts(
                organizationID = organizationID,
                appID = appId,
                apiKey = apiKey
            )

            when (productsResponse) {
                is NetworkResponse.Success -> {
                    val allProducts = productsResponse.payLoad ?: emptyList()
                    if (allProducts.isEmpty()) {
                        // Handle empty product list scenario
                        _uiState.update {
                            it.copy(
                                isLoading = false, errorMessage = "No products found"
                            )
                        }
                        _eventFlow.emit(UIEvents.SnackBarEvent(message = "No products found"))
                    } else {
                        withContext(dispatcher.Default) {
                            //val wishlist = localDatabaseRepository.getWishlist().first()
                            val groupedProductsByCategory = allProducts.flatMap { product ->
                                product.category.map { category ->
                                    category.name to product
                                }
                            }.groupBy({ it.first }, { it.second })

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
                    _eventFlow.emit(UIEvents.SnackBarEvent(message = productsResponse.message))
                }
            }
        } ?: run {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Network timeout"
                )
            }
            _eventFlow.emit(UIEvents.SnackBarEvent(message = "Network timeout"))
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

                product?.id?.let { Log.d("Product ID from n/w", it) }
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
            }.toList()
        }.toMutableMap()

        _uiState.update {
            it.copy(categoriesWithProducts = updatedCategories)
        }

        val updatedCartItems = getCartItems(updatedCategories)

        _uiState.update {
            it.copy(cartItems = updatedCartItems)
        }
    }

    fun updateQuantity(product: DomainProduct, newQuantity: Int) =
        viewModelScope.launch {
            val updatedCartItems =
                _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
                    products.map {
                        if (it.id == product.id) {
                            it.copy(
                                quantity = newQuantity
                                //price = newQuantity * it.price
                            )
                        } else {
                            it
                        }
                    }.toList()
                }.toMutableMap()

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

    fun clearCart() =
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cartItems = emptyList()
                )
            }
        }

    private fun getCartItems(categories: Map<String, List<DomainProduct>>): List<DomainProduct> =
        cartItemsCache.ifEmpty {
            categories.values.flatten().filter { it.isAddedToCart }
        }

    fun toggleWishlist(product: DomainWishlistItem) =
        viewModelScope.launch {
            // Check if the product is already in the wishlist
            val isProductAlreadyInWishlist = _uiState.value.wishlist.any { it.id == product.id }

            if (isProductAlreadyInWishlist) {
                // If the product is in the wishlist, remove it
                val result = localDatabaseRepository.removeFromWishlist(product.toWishListItem())

                when (result) {
                    is DatabaseResponse.Success -> {
                        // Update the UI state to reflect the product has been removed from the wishlist
                        val updatedCategories =
                            _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
                                products.map { p ->
                                    if (p.id == product.id) {
                                        p.copy(isAddedToWishlist = false) // Set to false after removing
                                    } else {
                                        p
                                    }
                                }
                            }
                        val updatedWishlist = _uiState.value.wishlist.toMutableList()
                        updatedWishlist.removeIf { it.id == product.id }

                        _uiState.update {
                            it.copy(
                                categoriesWithProducts = updatedCategories,
                                wishlist = updatedWishlist
                            )
                        }
                    }

                    is DatabaseResponse.Error -> {
                        _eventFlow.emit(UIEvents.SnackBarEvent(message = result.message))
                    }
                }
            } else {
                // If the product is not in the wishlist, add it
                val result = localDatabaseRepository.addToWishList(product.toWishListItem())

                when (result) {
                    is DatabaseResponse.Success -> {
                        // Update the UI state to reflect the product has been added to the wishlist
                        val updatedCategories =
                            _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
                                products.map { p ->
                                    if (p.id == product.id) {
                                        p.copy(isAddedToWishlist = true) // Set to true after adding
                                    } else {
                                        p
                                    }
                                }
                            }
                        val updatedWishlist = _uiState.value.wishlist.toMutableList()

                        _uiState.update {
                            it.copy(
                                categoriesWithProducts = updatedCategories,
                                wishlist = updatedWishlist
                            )
                        }
                    }

                    is DatabaseResponse.Error -> {
                        _eventFlow.emit(UIEvents.SnackBarEvent(message = result.message))
                    }
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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                        val updatedCategories =
                            _uiState.value.categoriesWithProducts.mapValues { (_, products) ->
                                products.map { p ->
                                    if (wishListResponse.items?.any { it.id == p.id } == true) {
                                        p.copy(isAddedToWishlist = true)
                                    } else {
                                        p
                                    }
                                }
                            }

                        Log.d("Wishlist", wishListResponse.items.toString())

                        _uiState.update {
                            it.copy(categoriesWithProducts = updatedCategories, wishlist = wishListResponse.items ?: emptyList())
                        }
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
            val isProductInWishlist =
                localDatabaseRepository.removeFromWishlist(product.toWishListItem())

            when (isProductInWishlist) {
                is DatabaseResponse.Success -> {
                    val updatedWishlist = _uiState.value.wishlist.toMutableList()
                    updatedWishlist.removeIf {
                        it.id == product.id
                    }

                    _uiState.update {
                        it.copy(wishlist = updatedWishlist)
                    }
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

    fun getOrderById(orderId: String) =
        viewModelScope.launch {
            localDatabaseRepository.getOrderById(orderId).collect{ order ->
                when(order){
                    is DatabaseResponse.Success -> {
                        _uiState.update {
                            it.copy(order = order.items)
                        }
                    }
                    is DatabaseResponse.Error -> {
                        _eventFlow.emit(UIEvents.SnackBarEvent(message = order.message))
                    }
                }
            }
        }
}