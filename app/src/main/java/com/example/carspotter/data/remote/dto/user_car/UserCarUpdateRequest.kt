package com.example.carspotter.data.remote.dto.user_car

import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserCarUpdateRequest(
    @Serializable(with = UUIDSerializer::class)
    val carModelId: UUID,
    val imagePath: String? = null,
)