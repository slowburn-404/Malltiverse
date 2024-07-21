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
    @ColumnInfo(name = "image_url")
    val imageURL: String = ""
)
