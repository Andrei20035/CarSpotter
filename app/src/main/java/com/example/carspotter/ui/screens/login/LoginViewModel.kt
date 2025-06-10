package com.example.carspotter.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Login screen that handles authentication logic and state management.
 */
@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

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
                isConfirmPasswordVisible = false
            )
        }
    }

    fun login() {
        viewModelScope.launch {
            // TODO: Implement actual login logic
            // For now, just simulate a successful login
            _uiState.update { it.copy(isLoading = true) }
            // Simulate network delay
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            // TODO: Implement actual sign up logic
            // For now, just simulate a successful sign up
            _uiState.update { it.copy(isLoading = true) }
            // Simulate network delay
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
        }
    }

    fun googleSignIn() {
        viewModelScope.launch {
            // TODO: Implement Google Sign In
            // For now, just simulate a successful sign in
            _uiState.update { it.copy(isLoading = true) }
            // Simulate network delay
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
        }
    }

    fun forgotPassword() {
        // TODO: Implement forgot password logic
    }
}

/**
 * Data class representing the UI state for the Login screen.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoginMode: Boolean = true,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)