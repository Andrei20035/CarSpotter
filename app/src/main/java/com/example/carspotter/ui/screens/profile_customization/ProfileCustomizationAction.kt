package com.example.carspotter.ui.screens.profile_customization

import android.net.Uri
import java.time.LocalDate

sealed class ProfileCustomizationAction {
    // Personal info actions
    data class UpdateProfileImage(val imageSource: ImageSource) : ProfileCustomizationAction()
    data class UpdateFullName(val fullName: String) : ProfileCustomizationAction()
    data class UpdateUsername(val username: String) : ProfileCustomizationAction()
    data class UpdateBirthDate(val birthDate: LocalDate): ProfileCustomizationAction()
    data class UpdateCountry(val country: String) : ProfileCustomizationAction()

    // Car info actions
    data class UpdateCarImage(val imageSource: ImageSource) : ProfileCustomizationAction()
    data class UpdateCarBrand(val brand: String) : ProfileCustomizationAction()
    data class UpdateCarModel(val model: String) : ProfileCustomizationAction()

    // Navigation actions
    object NextStep : ProfileCustomizationAction()
    object PreviousStep : ProfileCustomizationAction()
    object Complete : ProfileCustomizationAction()
}