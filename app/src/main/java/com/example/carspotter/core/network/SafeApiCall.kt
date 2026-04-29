package com.example.carspotter.core.network

import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let { ApiResult.Success(it) }
                ?: ApiResult.Error("Empty response")
        } else {
            ApiResult.Error(response.errorBody()?.string() ?: "Unknown error")
        }
    } catch (e: IOException) {
        ApiResult.Error("Network error: ${e.message}")
    } catch (e: Exception) {
        ApiResult.Error("Unexpected error: ${e.message}")
    }
}