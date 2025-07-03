package com.example.carspotter.ui.screens.feed

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carspotter.ui.screens.login.ScreenBackground
import com.example.carspotter.ui.screens.profile_customization.ProfileCustomizationViewModel

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    ScreenBackground {

    }
}