package com.example.carspotter.data.remote.model.auth

import com.example.carspotter.domain.model.AuthProvider
import kotlinx.serialization.Serializable

@Serializable
class LoginRequest (
    val email: String,
    val password: String? = null,
    val googleId: String? = null,
    val provider: AuthProvider
)