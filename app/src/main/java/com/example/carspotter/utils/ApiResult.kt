package com.example.carspotter.utils

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val exception: Exception?) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}