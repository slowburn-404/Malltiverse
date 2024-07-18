package dev.borisochieng.malltiverse.util

sealed class UIEvents {
    data class SnackBarEvent(val message: String): UIEvents()
}