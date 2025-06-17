package com.example.carspotter.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.repository.AuthRepository
import com.example.carspotter.domain.model.AuthProvider
import com.example.carspotter.utils.ApiResult
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    fun updateEmail(email: String?) {
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

    fun setProviderAndToken(googleId: String?, provider: AuthProvider) {
        _uiState.update {
            it.copy(
                provider = provider,
                googleId = googleId
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

    fun login(googleId: String? = null) {
        Log.d("LOGIN_DEBUG", "login() function called")
        val email = uiState.value.email ?: return
        val password = uiState.value.password
        val tokenToUse = googleId ?: uiState.value.googleId
        val provider = uiState.value.provider

        Log.d("UI_STATE", Gson().toJson(uiState.value))

        when (provider) {
            AuthProvider.REGULAR -> {
                if (!isValidEmail(email)) {
                    setError("Email is invalid")
                    return
                }
                if (password.isNullOrBlank()) {
                    setError("Password cannot be empty")
                    return
                }
                if (!isValidPassword(password)) {
                    setError("Password needs 10+ chars, 1 uppercase & special char")
                    return
                }
            }

            AuthProvider.GOOGLE -> {
                println("GoogleId " + tokenToUse)

                if (tokenToUse.isNullOrBlank()) {
                    setError("An error occurred: missing Google ID")
                    return
                }
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = authRepository.login(
                email = email,
                password = password,
                googleId = googleId,
                provider = provider
            )) {
                is ApiResult.Success -> _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                is ApiResult.Error -> {
                    Log.e("LoginError", "Login failed: ${result.message}")
                    setError("Login failed. Please try again.")
                }
                is ApiResult.Loading -> {
                    // already handled
                }
            }
        }
    }


    fun signUp() {
        val email = uiState.value.email ?: return
        val password = uiState.value.password
        val confirmPassword = uiState.value.confirmPassword
        val authProvider = uiState.value.provider

        if (!validateInputs(email, password, confirmPassword)) return

        val nonNullPassword = password!!
        val nonNullConfirmPassword = confirmPassword!!

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (authRepository.register(email, nonNullPassword, nonNullConfirmPassword, authProvider)) {
                is ApiResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
                }

                is ApiResult.Error -> {
                    setError("Registration failed. Please try again.")
                }

                ApiResult.Loading -> {
                    // Already handled by setting isLoading to true
                }
            }
        }
    }

    fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message, isLoading = false) }

        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(errorMessage = null) }
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

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        return emailRegex.matches(email)
    }

    private fun isValidPassword(password: String?): Boolean {
        val hasMinLength = password?.length!! >= 10
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return hasMinLength && hasUpperCase == true && hasSpecialChar == true
    }

    private fun validateInputs(email: String, password: String?, confirmPassword: String?): Boolean {
        return when {
            email.isBlank() || password.isNullOrBlank() -> {
                setError("Email and password cannot be empty")
                false
            }

            !isValidEmail(email) -> {
                setError("Email is invalid")
                false
            }

            confirmPassword != null && password != confirmPassword -> {
                setError("Passwords do not match")
                false
            }

            !isValidPassword(password) -> {
                setError("Password needs 10+ chars, 1 uppercase & special char")
                false
            }

            else -> true
        }
    }


}
