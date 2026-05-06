package com.example.carspotter.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carspotter.features.feed.FeedScreen
import com.example.carspotter.features.auth.AuthScreen
import com.example.carspotter.features.onboarding.OnboardingScreen
import com.example.carspotter.features.profile.dashboard.ProfileDashboardScreen
import com.example.carspotter.features.profile.customization.ProfileCustomization
import com.example.carspotter.features.settings.SettingsScreen

@Composable
fun CarSpotterNavigation(
    navController: NavHostController,
    startDestination: String,
    ) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                navController = navController,
                onComplete = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            AuthScreen(
                navController = navController
            )
        }

        composable(Screen.ProfileCustomization.route) {
            ProfileCustomization(
                navController = navController
            )
        }

        composable(Screen.Feed.route) {
            FeedScreen(
                navController = navController
            )
        }

        composable(Screen.Profile.route) {
            ProfileDashboardScreen(
                navController = navController
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController
            )
        }

        // Add other screens...
    }
}
