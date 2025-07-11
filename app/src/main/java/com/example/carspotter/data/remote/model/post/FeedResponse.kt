package com.example.carspotter.data.remote.model.post

import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse(
    val posts: List<PostDTO>,
    val nextCursor: FeedCursor?,
    val hasMore: Boolean
)


