package dev.borisochieng.malltiverse.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishListItem(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "image_url")
    val imageURL: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "isAddedToWishlist")
    val isAddedToWishList: Boolean,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "isAddedToCart")
    val isAddedToCart: Boolean,
    @ColumnInfo(name = "available_quantity")
    val availableQuantity: Int
)
