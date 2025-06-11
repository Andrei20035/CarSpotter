package com.example.carspotter.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Data class representing a password update request to the API.
 */
@Serializable
data class UpdatePasswordRequest(
    val currentPassword: String,
)