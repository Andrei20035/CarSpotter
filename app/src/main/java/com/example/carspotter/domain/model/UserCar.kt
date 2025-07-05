package com.example.carspotter.domain.model

import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import java.time.Instant
import java.util.UUID

data class UserCar(
    val id: UUID,
    val userId: UUID,
    val carModelId: UUID,
    val imagePath: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

fun UserCarDTO.toDomain(): UserCar = UserCar(
    id = id,
    userId = userId,
    carModelId = carModelId,
    imagePath = imagePath,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun List<UserCarDTO>.toDomain(): List<UserCar> {
    return map { it.toDomain() }
}