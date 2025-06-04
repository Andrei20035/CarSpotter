package com.example.carspotter.ui.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    @DrawableRes val imageRes: Int,
    val backgroundColor: Color = Color.Companion.Black
)