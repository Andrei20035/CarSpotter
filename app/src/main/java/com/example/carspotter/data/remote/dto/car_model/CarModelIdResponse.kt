package com.example.carspotter.data.remote.dto.car_model

import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CarModelIdResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)
