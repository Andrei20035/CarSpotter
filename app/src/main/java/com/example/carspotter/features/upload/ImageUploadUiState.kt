package com.example.carspotter.features.upload

import com.example.carspotter.features.profile.customization.ImageSource

data class ImageUploadUiState(
    val image: ImageSource,
    val brand: String,
    val model: String,
    val description: String? = null
)
