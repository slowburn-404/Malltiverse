package dev.borisochieng.malltiverse.domain.models

data class DomainWishlistItem(
    val id: String,
    val name: String,
    val description: String,
    val imageURL: String,
    val price: Double,
    val isAddedToCart: Boolean,
    val availableQuantity: Int,
    val quantity: Int,
    val isAddedToWishlist: Boolean
)
