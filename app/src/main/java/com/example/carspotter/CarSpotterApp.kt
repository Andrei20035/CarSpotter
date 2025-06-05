package com.example.carspotter

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.carspotter.ui.navigation.CarSpotterNavigation

@Composable
fun CarSpotterApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        CarSpotterNavigation(navController = navController)
    }
}