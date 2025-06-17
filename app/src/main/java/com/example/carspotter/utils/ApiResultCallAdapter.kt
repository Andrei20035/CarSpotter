package com.example.carspotter.utils

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A factory for creating CallAdapter instances that convert Retrofit responses to ApiResult.
 */
class ApiResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Check if the return type is Call
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        // Check if the Call has a generic type
        check(returnType is ParameterizedType) {
            "Call return type must be parameterized as Call<T>"
        }

        // Get the response type inside the Call
        val responseType = getParameterUpperBound(0, returnType)

        // Check if the response type is ApiResult
        if (getRawType(responseType) != ApiResult::class.java) {
            return null
        }

        // Check if ApiResult has a generic type
        check(responseType is ParameterizedType) {
            "Response must be parameterized as ApiResult<T>"
        }

        // Get the success type inside ApiResult
        val successType = getParameterUpperBound(0, responseType)

        return ApiResultCallAdapter<Any>(successType)
    }
}

/**
 * A CallAdapter that converts Retrofit responses to ApiResult.
 */
class ApiResultCallAdapter<T>(
    private val successType: Type
) : CallAdapter<T, Call<ApiResult<T>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<ApiResult<T>> {
        return ApiResultCall(call)
    }
}

/**
 * A Call implementation that wraps a regular Call and converts its response to ApiResult.
 */
class ApiResultCall<T>(
    private val delegate: Call<T>
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(ApiResult.Success(body))
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(
                                ApiResult.Error(
                                    Exception("Response body is null"),
                                    "Empty response"
                                )
                            )
                        )
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    callback.onResponse(
                        this@ApiResultCall,
                        Response.success(
                            ApiResult.Error(
                                Exception("API call failed with code ${response.code()}"),
                                errorMessage
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResult = when (t) {
                    is IOException -> ApiResult.Error(t, "Network error: ${t.message}")
                    else -> ApiResult.Error(t as Exception, "Unexpected error: ${t.message}")
                }
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun execute(): Response<ApiResult<T>> {
        return try {
            val response = delegate.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Response.success(ApiResult.Success(body))
                } else {
                    Response.success(
                        ApiResult.Error(
                            Exception("Response body is null"),
                            "Empty response"
                        )
                    )
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Response.success(
                    ApiResult.Error(
                        Exception("API call failed with code ${response.code()}"),
                        errorMessage
                    )
                )
            }
        } catch (e: IOException) {
            Response.success(ApiResult.Error(e, "Network error: ${e.message}"))
        } catch (e: Exception) {
            Response.success(ApiResult.Error(e, "Unexpected error: ${e.message}"))
        }
    }

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(delegate.clone())

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
