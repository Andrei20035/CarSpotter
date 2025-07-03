package com.example.carspotter.data.repository

import com.example.carspotter.utils.ApiResult
import retrofit2.Response
import java.io.IOException

/**
 * Base repository class that handles API responses and converts them to a sealed class for better error handling.
 */
abstract class BaseRepository {


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
                    ApiResult.Error( "Empty response")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                ApiResult.Error(errorMessage)
            }
        } catch (e: IOException) {
            ApiResult.Error("Network error: ${e.message}")
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message}")
        }
    }
}