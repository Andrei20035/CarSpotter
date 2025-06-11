package com.example.carspotter.data.remote.model.user_car

import kotlinx.serialization.Serializable

@Serializable
data class UserCarUpdateRequest(
    val carModelId: Int,
    val imagePath: String? = null,
)