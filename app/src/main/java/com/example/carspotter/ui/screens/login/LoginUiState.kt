package com.example.carspotter.ui.screens.login

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