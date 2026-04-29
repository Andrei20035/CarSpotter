package com.example.carspotter.data.repository

import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.remote.api.AuthApi
import com.example.carspotter.data.remote.dto.auth.AuthResponse
import com.example.carspotter.data.remote.dto.auth.LoginRequest
import com.example.carspotter.data.remote.dto.auth.RegisterRequest
import com.example.carspotter.data.remote.dto.auth.UpdatePasswordRequest
import com.example.carspotter.data.model.AuthProvider
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    suspend fun login(email: String, password: String?, googleIdToken: String?, provider: AuthProvider): ApiResult<AuthResponse>
    suspend fun register(email: String, password: String, confirmPassword: String, provider: AuthProvider): ApiResult<AuthResponse>
    suspend fun deleteAccount(): ApiResult<Unit>
    suspend fun updatePassword(currentPassword: String): ApiResult<Unit>
    suspend fun logout()
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String?, googleIdToken: String?, provider: AuthProvider): ApiResult<AuthResponse> {
        val loginRequest = LoginRequest(email, password, googleIdToken, provider)
        return safeApiCall { authApi.login(loginRequest) }
    }

    override suspend fun register(email: String, password: String, confirmPassword: String, provider: AuthProvider): ApiResult<AuthResponse> {
        val registerRequest = RegisterRequest(email, password, provider)
        return safeApiCall { authApi.register(registerRequest) }
    }

    override suspend fun deleteAccount(): ApiResult<Unit> {
        val result =  safeApiCall { authApi.deleteAccount() }

        if (result is ApiResult.Success) {
            userPreferences.clearAuthData()
        }

        return result
    }

    override suspend fun updatePassword(currentPassword: String): ApiResult<Unit> {
        val updatePasswordRequest = UpdatePasswordRequest(currentPassword)
        return safeApiCall { authApi.updatePassword(updatePasswordRequest) }
    }

    override suspend fun logout() {
        userPreferences.clearAuthData()
    }

}
