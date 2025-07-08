package com.example.carspotter.domain.model

import com.example.carspotter.data.remote.model.post.PostDTO
import java.time.Instant
import java.util.UUID

data class Post(
    val id: UUID,
    val userId: UUID,
    val carModelId: UUID,
    val imagePath: String,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

fun PostDTO.toDomain(): Post = Post(
    id = id,
    userId = userId,
    carModelId = carModelId,
    imagePath = imagePath,
    description = description ?: "",
    latitude = latitude,
    longitude = longitude,
    createdAt = createdAt ?: Instant.now(),
    updatedAt = updatedAt ?: Instant.now()
)

fun List<PostDTO>.toDomain(): List<Post> {
    return map { it.toDomain() }
}