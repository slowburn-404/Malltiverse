package dev.borisochieng.malltiverse.domain

import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem

fun DomainOrder.toOrder() =
    Order(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
    )

fun DomainProduct.toOrder() =
    Order(
        id = id,
        name = name,
        description = description,
        imageUrl = imageURL
    )

fun DomainWishlistItem.toWishListItem() =
    WishListItem(
        id = id,
        name = name,
        description = description,
        imageURL = imageURL
    )

fun DomainProduct.toDomainWishlistItem() =
    DomainWishlistItem(
        id = id,
        name = name,
        description = description,
        imageURL = imageURL
    )