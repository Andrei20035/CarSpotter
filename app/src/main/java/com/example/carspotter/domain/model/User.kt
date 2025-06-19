package com.example.carspotter.domain.model


import java.time.Instant
import java.time.LocalDate


data class User(
    val id: Int = 0,
    val authCredentialId: Int = 0,
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
