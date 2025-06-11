package com.example.carspotter.data.remote.model.car_model

import kotlinx.serialization.Serializable

@Serializable
data class CarModel(
    val id: Int = 0,
    val brand: String,
    val model: String,
    val year: Int? = null
)
