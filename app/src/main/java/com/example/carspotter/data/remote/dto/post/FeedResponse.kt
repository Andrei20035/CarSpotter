package com.example.carspotter.data.remote.dto.post

import com.example.carspotter.data.model.Post
import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse(
    val posts: List<Post>,
    val nextCursor: FeedCursor?,
    val hasMore: Boolean
)


