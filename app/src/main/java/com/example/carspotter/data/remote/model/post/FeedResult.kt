package com.example.carspotter.data.remote.model.post

import com.example.carspotter.domain.model.Post
import com.example.carspotter.domain.model.toDomain

data class FeedResult(
    val posts: List<Post>,
    val nextCursor: FeedCursor?,
    val hasMore: Boolean
)

fun FeedResponse.toDomain(): FeedResult = FeedResult(
    posts = posts.map { it.toDomain() },
    nextCursor = nextCursor,
    hasMore = hasMore
)
