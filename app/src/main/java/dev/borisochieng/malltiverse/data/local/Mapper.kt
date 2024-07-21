package dev.borisochieng.malltiverse.data.local

import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Order.toDomainOrder(): DomainOrder =
    DomainOrder(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        price = price,
        status = status,
        timeStamp = formatDate(timestamp)
    )

fun WishListItem.toDomainWishListItem(): DomainWishlistItem =
    DomainWishlistItem(
        id = id,
        name = name,
        description = description,
        imageURL = imageURL
    )


fun formatDate(timestamp: Date): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(timestamp)
}
