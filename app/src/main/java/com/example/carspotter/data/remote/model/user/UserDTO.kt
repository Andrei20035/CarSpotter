package com.example.carspotter.data.remote.model.user

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

@Serializable
data class UserDTO(
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val profilePicturePath: String? = null,
    val birthDate: LocalDate,
    val username: String,
    val country: String,
    val spotScore: Int = 0,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)