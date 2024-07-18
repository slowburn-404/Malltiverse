package dev.borisochieng.malltiverse.domain

import dev.borisochieng.malltiverse.data.NetworkResponse
import dev.borisochieng.malltiverse.domain.models.DomainProduct

interface TimbuAPIRepository {
    suspend fun getProducts(
        apiKey: String,
        organizationID: String,
        appID: String
    ): NetworkResponse<List<DomainProduct>>

}