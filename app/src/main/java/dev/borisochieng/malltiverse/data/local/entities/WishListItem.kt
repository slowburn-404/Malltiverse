package dev.borisochieng.malltiverse.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishListItem(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    @Ignore
    val isAddedToWishList: Boolean = false,
    @Ignore
    val quantity: Int = 0,
    @Ignore
    val isAddedToCart: Boolean = false,
    @Ignore
    val availableQuantity: Int = 0,
    @Ignore
    val imageURL: String
) {
    constructor() : this(
        "feofnw",
        "test",
        "test",
        100.0,
        true,
        10,
        true,
        10,
        "test"
    )

}
