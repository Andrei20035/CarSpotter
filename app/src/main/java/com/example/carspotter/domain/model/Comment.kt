package com.example.carspotter.domain.model

import com.example.carspotter.data.remote.model.comment.CommentDTO
import java.time.Instant
import java.util.UUID

data class Comment(
    val id: UUID,
    val userId: UUID,
    val postId: UUID,
    val commentText: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

fun CommentDTO.toDomain(): Comment = Comment(
    id = id,
    userId = userId,
    postId = postId,
    commentText = commentText,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun List<CommentDTO>.toDomain(): List<Comment> {
    return map { it.toDomain() }
}
