package com.example.carspotter.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfilePictureRequest(
    val imagePath: String
)