package com.example.carspotter.ui.screens.profile_customization

import android.net.Uri
import java.time.LocalDate

sealed class ImageSource {
    data class Local(val uri: Uri, val mimeType: String? = null) : ImageSource()
    data class Remote(val url: String, val mimeType: String? = null) : ImageSource()
}

sealed class ProfileStep {
    object Personal: ProfileStep()
    object Car: ProfileStep()
}

data class ProfileCustomizationUiState(
    val allBrands: List<String> = emptyList(),
    val modelsForSelectedBrand: List<String> = emptyList(),

    val profilePicture: ImageSource? = null,
    val fullName: String = "",
    val username: String = "",
    val country: String = "Romania",
    val birthDate: LocalDate? = null,

    val carPicture: ImageSource? = null,
    val selectedBrand: String = "",
    val selectedModel: String = "",
    val isFetchingBrands: Boolean = false,
    val isFetchingModels: Boolean = false,

    val currentStep: ProfileStep = ProfileStep.Car, // TODO: Modify in production
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserCreated: Boolean = false
)