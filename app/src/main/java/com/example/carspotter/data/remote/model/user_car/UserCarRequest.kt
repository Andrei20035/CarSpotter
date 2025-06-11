package com.example.carspotter.data.remote.model.user_car

import kotlinx.serialization.Serializable

@Serializable
data class UserCarRequest(
    val userId: Int,
    val carModelId: Int,
    val imagePath: String? = null,
)
