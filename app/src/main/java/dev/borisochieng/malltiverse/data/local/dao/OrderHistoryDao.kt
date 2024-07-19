package dev.borisochieng.malltiverse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.local.entities.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderHistoryDao {
    @Query("SELECT * FROM order_history")
    fun getAllOrders(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(order: Order)

    @Delete
    fun deleteOrder(order: Order)

}