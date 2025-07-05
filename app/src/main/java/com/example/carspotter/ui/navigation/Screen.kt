package com.example.carspotter.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object ProfileCustomization : Screen("profile_customization")
    object Feed : Screen("feed")
    object Map : Screen("map")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Camera : Screen("camera")
}