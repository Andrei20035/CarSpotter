package com.example.carspotter.features.auth

import com.example.carspotter.data.model.AuthProvider

sealed class AuthAction {
    data class EmailChanged(val email: String?) : AuthAction()
    data class PasswordChanged(val password: String) : AuthAction()
    data class ConfirmPasswordChanged(val password: String) : AuthAction()
    object TogglePasswordVisibility : AuthAction()
    object ToggleConfirmPasswordVisibility : AuthAction()
    data class Login(val googleId: String?, val provider: AuthProvider) : AuthAction()
    data class SignUp(val googleId: String?, val provider: AuthProvider) : AuthAction()
    object ForgotPassword : AuthAction()
    object ToggleMode : AuthAction()

    // For test only
    object ResetOnboarding : AuthAction()
    object SetAuthenticatedTrue: AuthAction()
}