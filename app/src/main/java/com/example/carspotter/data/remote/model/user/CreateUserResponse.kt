package com.example.carspotter.data.remote.model.user

import com.example.carspotter.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateUserResponse(
    val jwtToken: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
)
