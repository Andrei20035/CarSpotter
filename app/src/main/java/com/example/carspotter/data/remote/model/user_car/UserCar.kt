package com.example.carspotter.data.remote.model.user_car

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class UserCar(
    val id: Int = 0,
    val userId: Int,
    val carModelId: Int,
    val imagePath: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)