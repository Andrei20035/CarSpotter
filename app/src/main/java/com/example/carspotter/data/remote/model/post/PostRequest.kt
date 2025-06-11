package com.example.carspotter.data.remote.model.post

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val carModelId: Int,
    val imagePath: String,
    val description: String? = null,
)
