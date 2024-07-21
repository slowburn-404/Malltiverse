package dev.borisochieng.malltiverse.data.remote

sealed class NetworkResponse<out T> {
    class Success<out T>(val payLoad: T?) : NetworkResponse<T>()
    class Error(val message: String) : NetworkResponse<Nothing>()
}