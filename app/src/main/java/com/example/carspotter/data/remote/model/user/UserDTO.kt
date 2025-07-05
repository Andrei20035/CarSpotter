package com.example.carspotter.data.remote.model.user

import com.example.carspotter.serialization.LocalDateSerializer
import com.example.carspotter.serialization.InstantSerializer
import com.example.carspotter.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Serializable
data class UserDTO(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val fullName: String,
    val profilePicturePath: String? = null,
    val phoneNumber: String,
    val username: String,
    val country: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val spotScore: Int = 0,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null
)