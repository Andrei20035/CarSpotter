package com.example.carspotter.data.remote.model.comment

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CommentDTO(
    val id: Int = 0,
    val userId: Int,
    val postId: Int,
    val commentText: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)