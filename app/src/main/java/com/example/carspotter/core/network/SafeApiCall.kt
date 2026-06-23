package com.example.carspotter.core.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.Response
import java.io.IOException

suspend fun safeApiCallNoContent(apiCall: suspend () -> Response<Unit>): ApiResult<Unit> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            ApiResult.Success(Unit)
        } else {
            val errorBody = response.errorBody()?.string()
            ApiResult.Error(extractErrorMessage(errorBody))
        }
    } catch (e: IOException) {
        ApiResult.Error("Network error: ${e.message}")
    } catch (e: Exception) {
        ApiResult.Error("Unexpected error: ${e.message}")
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error("Empty response")
        } else {

            val errorBody = response.errorBody()?.string()
            ApiResult.Error(extractErrorMessage(errorBody))
        }

    } catch (e: IOException) {
        ApiResult.Error("Network error: ${e.message}")
    } catch (e: Exception) {
        ApiResult.Error("Unexpected error: ${e.message}")
    }
}

private fun extractErrorMessage(errorBody: String?): String {
    if (errorBody.isNullOrBlank()) return "Unknown error"
    val trimmedBody = errorBody.trimStart()
    val looksLikeJsonObject = trimmedBody.startsWith("{")
    val fallbackMessage = if (errorBody.length > 200 || trimmedBody.startsWith("<")) {
        "Server error"
    } else {
        errorBody
    }

    if (!looksLikeJsonObject) return fallbackMessage

    return try {
        val json = Json.parseToJsonElement(errorBody) as? JsonObject
        json?.get("error")?.jsonPrimitive?.contentOrNull?.ifBlank { null }
            ?: json?.get("message")?.jsonPrimitive?.contentOrNull?.ifBlank { null }
            ?: "Unknown error"
    } catch (_: Exception) {
        fallbackMessage
    }
}
