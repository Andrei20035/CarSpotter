package com.example.carspotter.data.repository

import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.local.auth.AuthTokens
import com.example.carspotter.data.local.auth.DeviceIdentity
import com.example.carspotter.data.local.auth.TokenStore
import com.example.carspotter.data.remote.api.AuthApi
import com.example.carspotter.data.remote.dto.auth.AuthResponse
import com.example.carspotter.data.remote.dto.auth.LoginRequest
import com.example.carspotter.data.remote.dto.auth.RegisterRequest
import com.example.carspotter.data.remote.dto.auth.UpdatePasswordRequest
import com.example.carspotter.data.model.AuthProvider
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import com.example.carspotter.core.network.safeApiCallNoContent
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    suspend fun login(email: String?, password: String?, googleIdToken: String?, provider: AuthProvider): ApiResult<AuthResponse>
    suspend fun register(email: String?, password: String?, googleIdToken: String?, provider: AuthProvider): ApiResult<AuthResponse>
    suspend fun deleteAccount(): ApiResult<Unit>
    suspend fun updatePassword(oldPassword: String, newPassword: String): ApiResult<AuthResponse>
    suspend fun logout(): ApiResult<Unit>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val userPreferences: UserPreferences,
    private val tokenStore: TokenStore? = null,
    private val deviceIdentity: DeviceIdentity? = null,
) : AuthRepository {

    override suspend fun login(email: String?, password: String?, googleIdToken: String?, provider: AuthProvider): ApiResult<AuthResponse> {
        val loginRequest = LoginRequest(
            email, password, googleIdToken, provider,
            deviceIdentity?.id.orEmpty(), deviceIdentity?.name.orEmpty()
        )
        return safeApiCall { authApi.login(loginRequest) }
    }

    override suspend fun register(email: String?, password: String?, googleIdToken: String?, provider: AuthProvider): ApiResult<AuthResponse> {
        val registerRequest = RegisterRequest(
            email, password, provider, googleIdToken,
            deviceIdentity?.id.orEmpty(), deviceIdentity?.name.orEmpty()
        )
        return safeApiCall { authApi.register(registerRequest) }
    }

    override suspend fun deleteAccount(): ApiResult<Unit> {
        val result =  safeApiCall { authApi.deleteAccount() }

        if (result is ApiResult.Success) {
            tokenStore?.clear()
            userPreferences.clearAuthData()
        }

        return result
    }

    override suspend fun updatePassword(oldPassword: String, newPassword: String): ApiResult<AuthResponse> {
        val updatePasswordRequest = UpdatePasswordRequest(oldPassword, newPassword)
        val result = safeApiCall { authApi.updatePassword(updatePasswordRequest) }
        if (result is ApiResult.Success) {
            tokenStore?.save(AuthTokens(result.data.accessToken, result.data.refreshToken))
        }
        return result
    }

    override suspend fun logout(): ApiResult<Unit> {
        val result = safeApiCallNoContent { authApi.logout() }
        tokenStore?.clear()
        userPreferences.clearAuthData()
        return result
    }

}
