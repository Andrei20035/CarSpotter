package com.example.carspotter.data.remote.model.post

import com.example.carspotter.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class PostDTO(
    val id: Int = 0,
    val userId: Int,
    val carModelId: Int,
    val imagePath: String,
    val description: String? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
)