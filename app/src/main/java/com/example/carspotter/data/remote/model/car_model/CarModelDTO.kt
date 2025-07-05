package com.example.carspotter.data.remote.model.car_model

import com.example.carspotter.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CarModelDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val brand: String,
    val model: String,
    val year: Int? = null
)
