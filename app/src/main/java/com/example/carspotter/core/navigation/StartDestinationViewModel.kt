package com.example.carspotter.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartDestinationViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val onboardingDone = userPreferences.onboardingCompleted.first()
            val token = userPreferences.authToken.first()
            val userId = userPreferences.userId.first()

            _startDestination.value = when {
                !onboardingDone -> Screen.Onboarding.route
                token.isNullOrBlank() -> Screen.Auth.route
                userId == null -> {
                    userPreferences.clearAuthData()
                    Screen.Auth.route
                }
                else -> Screen.Feed.route
            }
        }
    }
}
