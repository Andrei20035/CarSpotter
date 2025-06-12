package com.example.carspotter.domain.model

import java.time.Instant

data class Comment(
    val id: Int = 0,
    val userId: Int,
    val postId: Int,
    val commentText: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
