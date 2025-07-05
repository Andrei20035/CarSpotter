package com.example.carspotter.domain.model


import com.example.carspotter.data.remote.model.user.UserDTO
import java.time.Instant
import java.time.LocalDate
import java.util.UUID


data class User(
    val id: UUID,
    val profilePicturePath: String? = null,
    val fullName: String,
    val phoneNumber: String,
    val birthDate: LocalDate,
    val username: String,
    val country: String,
    val spotScore: Int = 0,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

fun UserDTO.toDomain(): User = User(
    id = id,
    profilePicturePath = profilePicturePath,
    fullName = fullName,
    phoneNumber = phoneNumber,
    birthDate = birthDate,
    username = username,
    country = country,
    spotScore = spotScore,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun List<UserDTO>.toDomain(): List<User> {
    return map { it.toDomain() }
}
