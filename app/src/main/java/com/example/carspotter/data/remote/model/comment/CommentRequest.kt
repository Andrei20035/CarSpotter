package com.example.carspotter.data.remote.model.comment

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val postId: Int,
    val commentText: String,
)