package com.example.carspotter.data.remote.dto.comment

import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CommentRequest(
    @Serializable(with = UUIDSerializer::class)
    val postId: UUID,
    val commentText: String,
)