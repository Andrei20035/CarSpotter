package com.example.carspotter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carspotter.ui.screens.feed.FeedScreen
import com.example.carspotter.ui.screens.login.LoginScreen
import com.example.carspotter.ui.screens.onboarding.OnboardingScreen
import com.example.carspotter.ui.screens.profile_customization.ProfileCustomization

@Composable
fun CarSpotterNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProfileCustomization.route
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
            LoginScreen(
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

        // Add other screens...
    }
}