package com.example.carspotter.domain.model

import com.example.carspotter.data.remote.model.car_model.CarModelDTO
import com.example.carspotter.data.remote.model.post.PostDTO
import java.util.UUID

data class CarModel(
    val id: UUID,
    val brand: String,
    val model: String,
    val year: Int? = null,
)

fun CarModelDTO.toDomain() : CarModel = CarModel(
    id = id,
    brand = brand,
    model = model,
    year = year
)

fun List<CarModelDTO>.toDomain(): List<CarModel> {
    return map { it.toDomain() }
}