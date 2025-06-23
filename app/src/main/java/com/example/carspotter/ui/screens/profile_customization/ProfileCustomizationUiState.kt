package com.example.carspotter.ui.screens.profile_customization

import android.net.Uri
import java.time.LocalDate

sealed class ImageSource {
    data class Local(val uri: Uri) : ImageSource()
    data class Remote(val url: String) : ImageSource()
}

sealed class ProfileStep {
    object Personal: ProfileStep()
    object Car: ProfileStep()
}

data class ProfileCustomizationUiState(
    val allBrands: List<String> = emptyList(),
    val modelsForSelectedBrand: List<String> = emptyList(),

    val profileImage: ImageSource? = null,
    val fullName: String = "",
    val username: String = "",
    val country: String = "",
    val birthDate: LocalDate? = null,

    val carImage: ImageSource? = null,
    val selectedBrand: String = "",
    val selectedModel: String = "",
    val isFetchingBrands: Boolean = false,
    val isFetchingModels: Boolean = false,

    val currentStep: ProfileStep = ProfileStep.Personal,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)