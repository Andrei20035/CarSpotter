package com.example.carspotter.core.auth

import com.example.carspotter.data.local.auth.TokenStore
import com.example.carspotter.data.local.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val tokenStore: TokenStore,
    private val userPreferences: UserPreferences,
) {
    private val _expired = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val expired = _expired.asSharedFlow()

    suspend fun expire(message: String = "Your session has expired. Please sign in again.") {
        tokenStore.clear()
        userPreferences.clearAuthData()
        _expired.emit(message)
    }
}
