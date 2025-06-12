package com.example.carspotter.domain.model

import java.time.Instant

data class UserCar(
    val id: Int = 0,
    val userId: Int,
    val carModelId: Int,
    val imagePath: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)