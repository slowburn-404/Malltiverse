package dev.borisochieng.malltiverse.data.local

import kotlinx.coroutines.flow.Flow

sealed class DatabaseResponse<out T> {
    class Success<out T>(val items: T?) : DatabaseResponse<T>()
    class Error(val message: String) : DatabaseResponse<Nothing>()
}