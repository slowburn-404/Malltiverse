package dev.borisochieng.malltiverse.data.repository

import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.local.dao.OrderHistoryDao
import dev.borisochieng.malltiverse.data.local.dao.WishListDao
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.data.local.toDomainOrder
import dev.borisochieng.malltiverse.data.local.toDomainWishListItem
import dev.borisochieng.malltiverse.domain.LocalDatabaseRepository
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainWishlistItem
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class LocalDatabaseRepositoryImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val orderHistoryDao: OrderHistoryDao,
    private val wishListDao: WishListDao
) : LocalDatabaseRepository {
    override suspend fun getOrders(): Flow<DatabaseResponse<List<DomainOrder>>> = flow {
        try {
            withContext(dispatcher.IO) {
                orderHistoryDao.getAllOrders().collect { allOrders ->
                    val domainOrders = allOrders.map { it.toDomainOrder() }
                    emit(DatabaseResponse.Success(domainOrders))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error(message = "Something went wrong please try again"))
        }
    }

    override suspend fun addOrder(order: Order): DatabaseResponse<Unit> =
        withContext(dispatcher.IO) {
            try {
                orderHistoryDao.insertOrder(order)
                DatabaseResponse.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                DatabaseResponse.Error(message = "Something went wrong please try again")
            }
        }

    override suspend fun deleteOrder(order: Order) =
        withContext(dispatcher.IO) {
            try {
                orderHistoryDao.deleteOrder(order)
                DatabaseResponse.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                DatabaseResponse.Error(message = "Something went wrong please try again")
            }
        }

    override suspend fun getWishlist(): Flow<DatabaseResponse<List<DomainWishlistItem>>> = flow {
        try {
            withContext(dispatcher.IO) {
                wishListDao.getWishList().collect { wishList ->
                    val domainWishlistProducts = wishList.map { it.toDomainWishListItem() }
                    emit(DatabaseResponse.Success(domainWishlistProducts))

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DatabaseResponse.Error(message = "Something went wrong please try again"))
        }
    }

    override suspend fun removeFromWishlist(wishListItem: WishListItem) =
        withContext(dispatcher.IO) {
            try {
                wishListDao.removeFromWishList(wishListItem)
                DatabaseResponse.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                DatabaseResponse.Error(message = "Something went wrong please try again")
            }
        }

    override suspend fun addToWishList(wishListItem: WishListItem) =
        withContext(dispatcher.IO) {
            try {
                wishListDao.addToWishList(wishListItem)
                DatabaseResponse.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                DatabaseResponse.Error(message = "Something went wrong please try again")
            }
        }
}