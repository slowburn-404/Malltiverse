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
        imageUrl = imageUrl
    )

fun WishListItem.toDomainWishListItem(): DomainWishlistItem =
    DomainWishlistItem(
        id = id,
        name = name,
        description = description,
        imageURL = imageURL
    )