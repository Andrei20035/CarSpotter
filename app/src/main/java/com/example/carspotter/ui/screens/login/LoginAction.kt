package com.example.carspotter.ui.screens.login

sealed class LoginAction {
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    data class ConfirmPasswordChanged(val password: String) : LoginAction()
    object TogglePasswordVisibility : LoginAction()
    object ToggleConfirmPasswordVisibility : LoginAction()
    object Login : LoginAction()
    object SignUp : LoginAction()
    data class GoogleSignIn(val idToken: String?) : LoginAction()
    object ForgotPassword : LoginAction()
    object ToggleMode : LoginAction()
    object ResetOnboarding : LoginAction()
}