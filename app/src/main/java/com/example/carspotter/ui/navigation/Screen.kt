package com.example.carspotter.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object ProfileCustomization : Screen("profile_customization")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Camera : Screen("camera")
    object Feed : Screen("feed")
}