package dev.borisochieng.malltiverse.data.remote.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SingleProduct(
    val name: String,
    val description: String?,
    @SerializedName("unique_id") val uniqueId: String,
    @SerializedName("url_slug") val urlSlug: String,
    @SerializedName("is_available") val isAvailable: Boolean,
    @SerializedName("is_service") val isService: Boolean,
    @SerializedName("previous_url_slugs") val previousUrlSlugs: List<String>?,
    val unavailable: Boolean,
    @SerializedName("unavailable_start") val unavailableStart: String?,
    @SerializedName("unavailable_end") val unavailableEnd: String?,
    val id: String,
    @SerializedName("parent_product_id") val parentProductId: String?,
    val parent: String?,
    @SerializedName("organization_id") val organizationId: String,
    @SerializedName("product_image") val productImage: List<Any?>,
    val categories: List<Category>?,
    @SerializedName("date_created") val dateCreated: String,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("user_id") val userId: String,
    val photos: List<Photo>,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("is_deleted") val isDeleted: Boolean,
    @SerializedName("available_quantity") val availableQuantity: Float?,
    @SerializedName("selling_price") val sellingPrice: Double?,
    @SerializedName("discounted_price") val discountedPrice: Double?,
    @SerializedName("buying_price") val buyingPrice: Double?,
    @SerializedName("extra_infos") val extraInfos: List<String>?
) : Serializable
