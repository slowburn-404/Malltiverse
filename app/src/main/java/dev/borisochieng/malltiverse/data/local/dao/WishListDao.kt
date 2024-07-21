package dev.borisochieng.malltiverse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishListDao {
    @Query("SELECT * FROM wishlist")
    fun getWishList(): Flow<List<WishListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToWishList(wishListItem: WishListItem)

    @Delete
    fun removeFromWishList(wishListItem: WishListItem)
}