package dev.borisochieng.malltiverse.domain

import android.icu.util.Calendar
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import java.util.Date

fun DomainOrder.toOrder() =
    Order(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        price = price,
        status = 1,
        timestamp = Date()
    )

fun DomainProduct.toOrder() =
    Order(
        id = id,
        name = name,
        description = description,
        imageUrl = imageURL,
        price = price.toString(),
        status = 1,
        timestamp = Date()
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