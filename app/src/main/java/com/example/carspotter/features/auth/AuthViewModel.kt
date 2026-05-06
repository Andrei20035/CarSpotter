package com.example.carspotter.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.model.AuthProvider
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String?) {
        _uiState.update {
            it.copy(
                email = email,
                provider = AuthProvider.REGULAR,
                googleIdToken = null
            )
        }
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

    fun setGoogleAuth(googleIdToken: String) {
        _uiState.update {
            it.copy(
                provider = AuthProvider.GOOGLE,
                googleIdToken = googleIdToken,
                errorMessage = null
            )
        }
    }

    fun toggleLoginMode() {
        _uiState.update {
            it.copy(
                isLoginMode = !it.isLoginMode,
                password = null,
                confirmPassword = null,
                isPasswordVisible = false,
                isConfirmPasswordVisible = false,
                errorMessage = null
            )
        }
    }

    fun login(googleIdToken: String? = null) {
        val email = uiState.value.email
        val password = uiState.value.password
        val tokenToUse = googleIdToken ?: uiState.value.googleIdToken
        val provider = uiState.value.provider

        val requestEmail = if (provider == AuthProvider.GOOGLE) null else email
        val requestPassword = if (provider == AuthProvider.GOOGLE) null else password


        when (provider) {
            AuthProvider.REGULAR -> {
                if (!isValidEmail(requestEmail)) {
                    setError("Email is invalid")
                    return
                }
                if (password.isNullOrBlank()) {
                    setError("Password cannot be empty")
                    return
                }
                if (!isValidPassword(requestPassword)) {
                    setError("Password must be at least 8 characters long and include an uppercase letter and a special character.")
                    return
                }
            }

            AuthProvider.GOOGLE -> {
                if (tokenToUse.isNullOrBlank()) {
                    setError("Missing Google ID token")
                    return
                }
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = authRepository.login(
                email = requestEmail,
                password = requestPassword,
                googleIdToken = tokenToUse,
                provider = provider
            )) {
                is ApiResult.Success ->  {
                    userPreferences.saveJwtToken(result.data.token)
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is ApiResult.Error -> {
                    setError(result.message)
                }
            }
        }
    }


    fun signUp() {
        val email = uiState.value.email
        val password = uiState.value.password
        val confirmPassword = uiState.value.confirmPassword

        if (!validateInputs(email, password, confirmPassword)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = authRepository.register(email, password, null,AuthProvider.REGULAR)) {
                is ApiResult.Success -> {
                    userPreferences.saveJwtToken(result.data.token)
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                }

                is ApiResult.Error -> {
                    setError(result.message)
                }
            }
        }
    }


    fun forgotPassword() {
        // TODO: Implement forgot password logic
        // This would typically involve sending a password reset email
        val email = uiState.value.email ?: return

        if (email.isBlank()) {
            setError(message = "Please enter your email address")
            return
        }

        setError(message = "Password reset functionality not implemented yet")
    }


    private fun isValidEmail(email: String?): Boolean {
        if(email.isNullOrBlank()) {
            return false
        }
        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        return emailRegex.matches(email)
    }

    private fun isValidPassword(password: String?): Boolean {
        return !password.isNullOrBlank() && password.length >= 8
    }

    private fun validateInputs(email: String?, password: String?, confirmPassword: String?): Boolean {

        if (email.isNullOrBlank()) {
            setError("Email cannot be empty")
            return false
        }

        if (!isValidEmail(email)) {
            setError("Invalid email format")
            return false
        }

        if (password.isNullOrBlank()) {
            setError("Password cannot be empty")
            return false
        }

        if (!isValidPassword(password)) {
            setError("Password must be at least 8 characters long")
            return false
        }

        if (confirmPassword != null) {
            if (confirmPassword.isBlank()) {
                setError("Please confirm your password")
                return false
            }

            if (password != confirmPassword) {
                setError("Passwords do not match")
                return false
            }
        }

        return true
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message, isLoading = false) }

        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun setRegularAuth() {
        _uiState.update {
            it.copy(
                provider = AuthProvider.REGULAR,
                googleIdToken = null,
                errorMessage = null
            )
        }
    }

    /**
     * Resets the onboarding status to false and updates the UI state.
     * This is useful for testing the onboarding flow without having to uninstall the app.
     *
     * Only for testing purposes
     */
    fun resetOnboardingStatus(onComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferences.resetOnboardingStatus()
            onComplete()
        }
    }

    fun setAuthenticatedTrue() {
        _uiState.update {  it.copy(isAuthenticated = true) }
    }

}
