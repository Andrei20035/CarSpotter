package com.example.carspotter.data.remote.dto.user

import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateUserResponse(
    val accessToken: String,
    val refreshToken: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
)
