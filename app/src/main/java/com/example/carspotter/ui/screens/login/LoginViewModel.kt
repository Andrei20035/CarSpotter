package com.example.carspotter.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.repository.AuthRepository
import com.example.carspotter.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun toggleLoginMode() {
        _uiState.update { 
            it.copy(
                isLoginMode = !it.isLoginMode,
                password = "",
                confirmPassword = "",
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                errorMessage = null
            )
        }
    }

    fun login() {
        val email = uiState.value.email
        val password = uiState.value.password
        val googleId = uiState.value.googleId
        val provider = uiState.value.provider

        // Basic validation
        if (email.isBlank() || password?.isBlank() == true) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = authRepository.login(email, password, googleId, provider)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is ApiResult.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            errorMessage = result.message ?: "Login failed. Please try again."
                        )
                    }
                }
                ApiResult.Loading -> {
                    // Already handled by setting isLoading to true
                }
            }
        }
    }

    fun signUp() {
        val email = uiState.value.email
        val password = uiState.value.password
        val confirmPassword = uiState.value.confirmPassword
        val authProvider = uiState.value.provider

        // Basic validation
        if (email.isBlank() || password?.isBlank() == true) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty") }
            return
        }

        if (password != confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }

        if (password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Extract username from email for simplicity
            val username = email.substringBefore("@")

            when (val result = authRepository.register(email, password, confirmPassword, authProvider)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is ApiResult.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            errorMessage = result.message ?: "Registration failed. Please try again."
                        )
                    }
                }
                ApiResult.Loading -> {
                    // Already handled by setting isLoading to true
                }
            }
        }
    }

    fun googleSignIn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // TODO: Implement actual Google Sign In
            // For now, just simulate a successful sign in
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
        }
    }

    fun forgotPassword() {
        // TODO: Implement forgot password logic
        // This would typically involve sending a password reset email
        val email = uiState.value.email

        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your email address") }
            return
        }

        _uiState.update { it.copy(errorMessage = "Password reset functionality not implemented yet") }
    }

    /**
     * Resets the onboarding status to false and updates the UI state.
     * This is useful for testing the onboarding flow without having to uninstall the app.
     * 
     * @param onComplete Callback to be invoked when the reset is complete
     */
    fun resetOnboardingStatus(onComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferences.resetOnboardingStatus()
            onComplete()
        }
    }
}
