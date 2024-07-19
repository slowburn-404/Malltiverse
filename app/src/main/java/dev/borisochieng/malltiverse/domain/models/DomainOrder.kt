package dev.borisochieng.malltiverse.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class DomainOrder(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val isAddedToCart: Boolean
)
