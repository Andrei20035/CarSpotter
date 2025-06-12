package com.example.carspotter.utils

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception, val message: String? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}