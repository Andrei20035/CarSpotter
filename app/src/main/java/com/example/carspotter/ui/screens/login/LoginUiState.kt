package com.example.carspotter.ui.screens.login

import com.example.carspotter.domain.model.AuthProvider

data class LoginUiState(
    val email: String? = null,
    val password: String? = null,
    val googleIdToken: String? = null,
    val provider: AuthProvider = AuthProvider.REGULAR,
    val confirmPassword: String? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoginMode: Boolean = true,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)