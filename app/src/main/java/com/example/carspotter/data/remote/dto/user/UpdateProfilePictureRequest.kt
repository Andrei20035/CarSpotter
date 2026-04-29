package com.example.carspotter.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfilePictureRequest(
    val imagePath: String?
)