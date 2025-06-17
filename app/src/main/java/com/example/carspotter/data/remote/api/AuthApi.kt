package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.auth.AuthResponse
import com.example.carspotter.data.remote.model.auth.LoginRequest
import com.example.carspotter.data.remote.model.auth.RegisterRequest
import com.example.carspotter.data.remote.model.auth.UpdatePasswordRequest
import com.example.carspotter.utils.ApiResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT


interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResult<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResult<AuthResponse>

    @DELETE("auth/account")
    suspend fun deleteAccount(): ApiResult<Unit>

    @PUT("auth/password")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest): ApiResult<Unit>
}
