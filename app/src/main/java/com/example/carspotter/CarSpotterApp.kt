package com.example.carspotter

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.carspotter.core.navigation.CarSpotterNavigation
import com.example.carspotter.core.navigation.StartDestinationViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarSpotterApp : Application()

@Composable
fun CarSpotterAppUI(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startVm: StartDestinationViewModel = hiltViewModel()
    val start by startVm.startDestination.collectAsState()

    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        when (val s = start) {
            null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else -> CarSpotterNavigation(navController, s)
        }
    }
}