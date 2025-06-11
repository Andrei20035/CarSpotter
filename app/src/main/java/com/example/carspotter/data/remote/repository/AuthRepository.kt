package com.example.carspotter.data.remote.repository

import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.remote.api.AuthApi
import com.example.carspotter.data.remote.model.auth.AuthProvider
import com.example.carspotter.data.remote.model.auth.AuthResponse
import com.example.carspotter.data.remote.model.auth.LoginRequest
import com.example.carspotter.data.remote.model.auth.RegisterRequest
import com.example.carspotter.data.remote.model.auth.UpdatePasswordRequest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun login(email: String, password: String?, googleId: String?, provider: AuthProvider): ApiResult<AuthResponse> {
        val loginRequest = LoginRequest(email, password, googleId, provider)
        val result = safeApiCall { authApi.login(loginRequest) }
        
        if (result is ApiResult.Success) {
            saveAuthData(result.data)
        }
        
        return result
    }

    suspend fun register(email: String, password: String?, provider: AuthProvider): ApiResult<AuthResponse> {
        val registerRequest = RegisterRequest(email, password, provider)
        val result = safeApiCall { authApi.register(registerRequest) }
        
        if (result is ApiResult.Success) {
            saveAuthData(result.data)
        }
        
        return result
    }

    suspend fun deleteAccount(): ApiResult<Unit> {
        val result = safeApiCall { authApi.deleteAccount() }
        
        if (result is ApiResult.Success) {
            userPreferences.clearAuthData()
        }
        
        return result
    }

    suspend fun updatePassword(currentPassword: String): ApiResult<Unit> {
        val updatePasswordRequest = UpdatePasswordRequest(currentPassword)
        return safeApiCall { authApi.updatePassword(updatePasswordRequest) }
    }

    suspend fun logout() {
        userPreferences.clearAuthData()
    }

    private suspend fun saveAuthData(authResponse: AuthResponse) {
        userPreferences.saveAuthToken(authResponse.token)
    }
}