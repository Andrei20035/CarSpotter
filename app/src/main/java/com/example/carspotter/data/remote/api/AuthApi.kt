package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.dto.auth.AuthResponse
import com.example.carspotter.data.remote.dto.auth.AuthRequest
import com.example.carspotter.data.remote.dto.auth.UpdatePasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT


interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: AuthRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: AuthRequest): Response<AuthResponse>

    @DELETE("auth/account")
    suspend fun deleteAccount(): Response<Unit>

    @PUT("auth/password")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest): Response<Unit>
}
