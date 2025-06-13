package com.example.carspotter.ui.screens.login

import com.example.carspotter.domain.model.AuthProvider

sealed class LoginAction {
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    data class ConfirmPasswordChanged(val password: String) : LoginAction()
    object TogglePasswordVisibility : LoginAction()
    object ToggleConfirmPasswordVisibility : LoginAction()
    data class Login(val googleId: String?, val provider: AuthProvider) : LoginAction()
    data class SignUp(val googleId: String?, val provider: AuthProvider) : LoginAction()
    object ForgotPassword : LoginAction()
    object ToggleMode : LoginAction()
    object ResetOnboarding : LoginAction()
}