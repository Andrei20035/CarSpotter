package com.example.carspotter

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.carspotter.ui.navigation.CarSpotterNavigation
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarSpotterApp : Application()

@Composable
fun CarSpotterAppUI(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        CarSpotterNavigation(navController = navController)
    }
}