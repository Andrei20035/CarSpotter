package com.example.carspotter.ui.screens.login

import com.example.carspotter.domain.model.AuthProvider

/**
 * Data class representing the UI state for the Login screen.
 */
data class LoginUiState(
    val email: String = "",
    val password: String? = null,
    val googleId: String? = null,
    val provider: AuthProvider = AuthProvider.REGULAR,
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoginMode: Boolean = true,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)