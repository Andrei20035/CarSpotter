package com.example.carspotter.ui.screens.image_upload

import com.example.carspotter.ui.screens.profile_customization.ImageSource

data class ImageUploadUiState(
    val image: ImageSource,
    val brand: String,
    val model: String,
    val description: String? = null
)
