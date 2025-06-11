package com.example.carspotter.data.remote.model.post

import kotlinx.serialization.Serializable

@Serializable
data class PostEditRequest(
    val newDescription: String? = null,
)