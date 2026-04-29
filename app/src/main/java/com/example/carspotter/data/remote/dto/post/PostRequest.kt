package com.example.carspotter.data.remote.dto.post

import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PostRequest(
    @Serializable(with = UUIDSerializer::class)
    val carModelId: UUID,
    val imagePath: String,
    val description: String? = null,
)
