package com.example.carspotter.data.repository

import retrofit2.Response
import java.io.IOException

/**
 * Base repository class that handles API responses and converts them to a sealed class for better error handling.
 */
abstract class BaseRepository {

    /**
     * Sealed class representing the result of an API call.
     */
    sealed class ApiResult<out T> {
        data class Success<out T>(val data: T) : ApiResult<T>()
        data class Error(val exception: Exception, val message: String? = null) : ApiResult<Nothing>()
        object Loading : ApiResult<Nothing>()
    }

    /**
     * Executes a safe API call and returns the result wrapped in ApiResult.
     *
     * @param apiCall The suspend function to execute
     * @return ApiResult containing either the success data or error information
     */
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error(Exception("Response body is null"), "Empty response")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(Exception("API call failed with code ${response.code()}"), errorMessage)
            }
        } catch (e: IOException) {
            ApiResult.Error(e, "Network error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error(e, "Unexpected error: ${e.message}")
        }
    }
}