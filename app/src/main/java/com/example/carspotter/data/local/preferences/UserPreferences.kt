package com.example.carspotter.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")
        val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USERNAME_KEY = stringPreferencesKey("username")
        val EMAIL_KEY = stringPreferencesKey("email")
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { it[ONBOARDING_KEY] ?: false }

    val authToken: Flow<String?> = context.dataStore.data
        .map { it[AUTH_TOKEN_KEY] }

    val userId: Flow<String?> = context.dataStore.data
        .map { it[USER_ID_KEY] }

    val username: Flow<String?> = context.dataStore.data
        .map { it[USERNAME_KEY] }

    val email: Flow<String?> = context.dataStore.data
        .map { it[EMAIL_KEY] }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[ONBOARDING_KEY] = completed }
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[AUTH_TOKEN_KEY] = token }
    }

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { it[USER_ID_KEY] = id }
    }

    suspend fun saveUsername(name: String) {
        context.dataStore.edit { it[USERNAME_KEY] = name }
    }

    suspend fun saveEmail(userEmail: String) {
        context.dataStore.edit { it[EMAIL_KEY] = userEmail }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit {
            it.remove(AUTH_TOKEN_KEY)
            it.remove(USER_ID_KEY)
            it.remove(USERNAME_KEY)
            it.remove(EMAIL_KEY)
        }
    }

    /**
     * Resets the onboarding status to false.
     * This is useful for testing the onboarding flow without having to uninstall the app.
     */
    suspend fun resetOnboardingStatus() {
        setOnboardingCompleted(false)
    }
}
