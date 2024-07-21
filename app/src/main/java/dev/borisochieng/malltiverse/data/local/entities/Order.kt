package dev.borisochieng.malltiverse.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "order_history")
data class Order(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: String,
    val status: Int,
    val timestamp: Date
)
