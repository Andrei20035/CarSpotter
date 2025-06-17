package com.example.carspotter.ui.screens.profile_setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carspotter.ui.theme.CarSpotterTheme

@Composable
fun ProfileSetup(
    navController: NavController,
    viewModel: ProfileSetupViewModel = hiltViewModel()
) {
    CarSpotterTheme {
        Box {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Profile Setup")
            }
        }
    }
}