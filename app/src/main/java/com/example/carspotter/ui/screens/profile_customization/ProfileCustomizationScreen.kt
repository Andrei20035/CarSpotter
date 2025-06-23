package com.example.carspotter.ui.screens.profile_customization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carspotter.ui.screens.login.ScreenBackground

@Composable
fun ProfileCustomization(
    navController: NavController,
    viewModel: ProfileCustomizationViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    when  (uiState.currentStep) {}
}