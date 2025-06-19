package com.example.carspotter.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Data class representing the response from authentication endpoints.
 */
@Serializable
data class AuthResponse(
    val jwtToken: String,
)