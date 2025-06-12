package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.auth.AuthResponse
import com.example.carspotter.domain.model.AuthProvider
import com.example.carspotter.utils.ApiResult

interface IAuthRepository {
    suspend fun login(email: String, password: String?, googleId: String?, provider: AuthProvider): ApiResult<AuthResponse>
    suspend fun register(email: String, password: String, confirmPassword: String, provider: AuthProvider): ApiResult<AuthResponse>
    suspend fun deleteAccount(): ApiResult<Unit>
    suspend fun updatePassword(currentPassword: String): ApiResult<Unit>
    suspend fun logout()
}