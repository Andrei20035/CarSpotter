package com.example.carspotter.data.remote.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UploadImageRequest(
    val imageName: String
)
