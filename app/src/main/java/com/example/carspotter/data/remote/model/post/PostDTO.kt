package com.example.carspotter.data.remote.model.post

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class PostDTO(
    val id: Int = 0,
    val userId: Int,
    val carModelId: Int,
    val imagePath: String,
    val description: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)