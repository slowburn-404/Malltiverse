package dev.borisochieng.malltiverse.util

import dev.borisochieng.malltiverse.domain.models.DomainProduct

data class UIState(
    val isLoading: Boolean = false,
    val products: List<DomainProduct> = emptyList(),
    val cartItems: List<DomainProduct> = emptyList(),
    val categoriesWithProducts: Map<String, List<DomainProduct>> = emptyMap(),
    val errorMessage: String = ""
)
