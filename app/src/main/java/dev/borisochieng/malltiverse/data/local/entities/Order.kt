package dev.borisochieng.malltiverse.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_history")
data class Order(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
)
