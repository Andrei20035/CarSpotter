package com.example.carspotter.data.remote.model.auth

import com.example.carspotter.domain.model.AuthProvider
import kotlinx.serialization.Serializable

/**
 * Data class representing a registration request to the API.
 */
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String?,
    val provider: AuthProvider
)