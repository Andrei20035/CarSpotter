package com.example.carspotter.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val isOnboardingCompleted: Flow<Boolean> = userPreferences.onboardingCompleted

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferences.setOnboardingCompleted(true)
        }
    }
}
