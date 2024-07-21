package dev.borisochieng.malltiverse.util

import androidx.compose.runtime.Immutable
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem

@Immutable
data class UIState(
    val isLoading: Boolean = false,
    val products: List<DomainProduct> = emptyList(),
    val cartItems: List<DomainProduct> = emptyList(),
    val categoriesWithProducts: Map<String, List<DomainProduct>> = emptyMap(),
    val orderHistory: List<DomainOrder> = emptyList(),
    val wishlist: List<DomainWishlistItem> = emptyList(),
    val product: DomainProduct? = null,
    val errorMessage: String = ""
)
