package dev.borisochieng.malltiverse.domain

import dev.borisochieng.malltiverse.data.local.DatabaseResponse
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.data.remote.NetworkResponse
import dev.borisochieng.malltiverse.domain.models.DomainOrder
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import kotlinx.coroutines.flow.Flow

interface TimbuAPIRepository {
    suspend fun getProducts(
        apiKey: String,
        organizationID: String,
        appID: String
    ): NetworkResponse<List<DomainProduct>>

}