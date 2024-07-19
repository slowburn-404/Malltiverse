package dev.borisochieng.malltiverse.data.local

import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem

fun Order.toDomainOrder(): DomainOrder =
    DomainOrder(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        isAddedToCart = false
    )

fun WishListItem.toDomainWishListItem(): DomainWishlistItem =
    DomainWishlistItem(
        id = id,
        name = name,
        description = description,
        imageURL = imageURL,
        price = price,
        isAddedToCart = false,
        quantity = quantity,
        availableQuantity = availableQuantity,
        isAddedToWishlist = isAddedToWishList
    )