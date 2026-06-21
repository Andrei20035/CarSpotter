package com.example.carspotter.core.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object ProfileCustomization : Screen("profile_customization")
    object Feed : Screen("feed")
    object Settings : Screen("settings")
    object Map : Screen("map")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Camera : Screen("camera")

    /**
     * Upload-image screen reached after the user captures/picks a photo from the
     * "Post your find" overlay. Carries the selected image URI as a query argument.
     */
    object ImageUpload : Screen("image_upload?imageUri={imageUri}") {
        const val ARG_IMAGE_URI = "imageUri"

        /** Builds the concrete route for [imageUri], encoding it for safe nav-arg transport. */
        fun createRoute(imageUri: String): String =
            "image_upload?imageUri=${Uri.encode(imageUri)}"
    }
}
