package com.example.carspotter.data.remote.dto.auth

import com.example.carspotter.data.model.AuthProvider
import kotlinx.serialization.Serializable

@Serializable
class LoginRequest (
    val email: String,
    val password: String? = null,
    val googleIdToken: String? = null,
    val provider: AuthProvider
)