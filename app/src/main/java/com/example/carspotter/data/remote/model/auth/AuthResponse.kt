package com.example.carspotter.data.remote.model.auth

import com.example.carspotter.data.remote.model.user.User
import kotlinx.serialization.Serializable

/**
 * Data class representing the response from authentication endpoints.
 */
@Serializable
data class AuthResponse(
    val token: String,
)