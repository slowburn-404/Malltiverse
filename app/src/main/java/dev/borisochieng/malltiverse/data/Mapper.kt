package dev.borisochieng.malltiverse.data

import dev.borisochieng.malltiverse.data.models.CurrentPrice
import dev.borisochieng.malltiverse.data.models.ProductResponse
import dev.borisochieng.malltiverse.domain.models.DomainCategory
import dev.borisochieng.malltiverse.domain.models.DomainProduct
import dev.borisochieng.malltiverse.util.Constants.BASE_IMAGE_URL

fun ProductResponse.toDomainProduct(): List<DomainProduct> =
    items.map { product ->
        DomainProduct(
            id = product.id,
            name = product.name,
            description = product.description ?: "No description available",
            price = extractPrice(product.currentPrice),
            imageURL = "$BASE_IMAGE_URL${product.photos?.firstOrNull()?.url}",
            category = product.categories?.map { category ->
                DomainCategory(
                    name = category.name.capitalizeWords(),
                )
            } ?: emptyList(),
            availableQuantity = product.availableQuantity?.toInt() ?: 0,
            quantity = 1,
            isAddedToCart = false
        )
    }

fun String.capitalizeWords(): String =
    split(" ")
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

fun extractPrice(priceList: List<CurrentPrice>): Double =
    priceList
        .flatMap { it.currency }
        .filterIsInstance<Double>()
        .firstOrNull()
        ?: 0.0
