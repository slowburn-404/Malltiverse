package dev.borisochieng.malltiverse.domain

import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import kotlinx.coroutines.flow.Flow

interface LocalDatabaseRepository {
    suspend fun getOrders(): Flow<DatabaseResponse<List<DomainOrder>>>

    suspend fun addOrder(order: Order): DatabaseResponse<Unit>

    suspend fun deleteOrder(order: Order): DatabaseResponse<Unit>

    suspend fun getWishlist(): Flow<DatabaseResponse<List<DomainWishlistItem>>>

    suspend fun removeFromWishlist(wishListItem: WishListItem): DatabaseResponse<Unit>

    suspend fun addToWishList(wishListItem: WishListItem): DatabaseResponse<Unit>

    suspend fun getOrderById(id: String): Flow<DatabaseResponse<DomainOrder>>
}