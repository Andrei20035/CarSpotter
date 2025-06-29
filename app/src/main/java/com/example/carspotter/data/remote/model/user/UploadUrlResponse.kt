package com.example.carspotter.data.remote.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UploadUrlResponse(
    val uploadUrl: String,
    val publicUrl: String,
)
