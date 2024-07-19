package dev.borisochieng.malltiverse.data.repository

import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.local.dao.OrderHistoryDao
import dev.borisochieng.malltiverse.data.local.dao.WishListDao
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.data.remote.NetworkResponse
import dev.borisochieng.malltiverse.data.remote.service.TimbuAPIService
import dev.borisochieng.malltiverse.data.remote.toDomainProduct
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.domain.TimbuAPIRepository
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TimbuAPIRepositoryImpl(
    private val apiService: TimbuAPIService,
    private val dispatcher: CoroutineDispatcherProvider
) : TimbuAPIRepository {
    override suspend fun getProducts(
        apiKey: String,
        organizationID: String,
        appID: String
    ): NetworkResponse<List<DomainProduct>> =
        withContext(dispatcher.IO) {
            try {
                val response = apiService.getProducts(apiKey, organizationID, appID)

                if (response.isSuccessful) {
                    val body = response.body()
                    NetworkResponse.Success(body?.toDomainProduct())
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "We encountered a problem with your request. Please try again."
                        401 -> "You need to log in to access this feature. Please log in and try again."
                        403 -> "You don't have permission to access this resource. Please contact support if you believe this is an error."
                        404 -> "The resource you're looking for couldn't be found. It might have been removed or is temporarily unavailable."
                        405 -> "The method you are trying to use is not allowed. Please check and try again."
                        408 -> "The request took too long to process. Please try again later."
                        429 -> "You have made too many requests in a short period. Please try again later."
                        500 -> "Something went wrong on our end. Please try again later."
                        502 -> "The server received an invalid response. Please try again later."
                        503 -> "The service is currently unavailable. Please try again later."
                        504 -> "The server took too long to respond. Please try again later."
                        else -> "An unexpected error occurred. Please try again."
                    }
                    NetworkResponse.Error(errorMessage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse.Error("An unexpected error occurred. Please try again later.")
            }
        }
}