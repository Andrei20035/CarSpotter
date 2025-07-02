package com.example.carspotter.ui.screens.profile_customization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ProfileCustomization(
    navController: NavController,
    viewModel: ProfileCustomizationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.currentStep) {
        is ProfileStep.Personal -> {
            PersonalInfoStep(
                viewModel = viewModel
            )
        }
        is ProfileStep.Car -> {
            CarInfoStep(
                viewModel = viewModel,
                navController = navController,
            )
        }
    }
}