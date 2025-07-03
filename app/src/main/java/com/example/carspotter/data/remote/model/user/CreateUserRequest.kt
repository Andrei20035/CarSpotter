package com.example.carspotter.data.remote.model.user

import com.example.carspotter.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CreateUserRequest(
    val profilePicturePath: String? = null,
    val fullName: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val phoneNumber: String? = null,
    val username: String,
    val country: String
)