package com.example.carspotter.data.remote.model.comment

import com.example.carspotter.serialization.InstantSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CommentDTO(
    val id: Int = 0,
    val userId: Int,
    val postId: Int,
    val commentText: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
)