package com.example.carspotter.data.remote.model.user

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CreateUserRequest(
    val profilePicturePath: String? = null,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val username: String,
    val country: String
)