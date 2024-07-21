package dev.borisochieng.malltiverse.data.remote.service

import dev.borisochieng.malltiverse.data.remote.models.Product
import dev.borisochieng.malltiverse.data.remote.models.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TimbuAPIService {

    @GET("/products")
    suspend fun getProducts(
        @Query("Apikey") apiKey: String,
        @Query("organization_id") organizationID: String,
        @Query("Appid") appID: String,
    ): Response<ProductResponse>

    @GET("/product/{productID}")
    suspend fun getProduct(
        @Query("product_id") productID: String,
        @Query("organization_id") organizationID: String,
        @Query("Appid") appID: String,
        @Query("Apikey") apiKey: String,

    ): Response<Product>
}