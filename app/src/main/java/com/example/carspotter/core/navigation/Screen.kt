package com.example.carspotter.core.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object ProfileCustomization : Screen("profile_customization")
    object Feed : Screen("feed")
    object Settings : Screen("settings")
    object Map : Screen("map")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Camera : Screen("camera")
}
