package com.example.carspotter.data.remote.dto.post

import com.example.carspotter.data.model.Post

data class FeedResult(
    val posts: List<Post>,
    val nextCursor: FeedCursor?,
    val hasMore: Boolean
)

fun FeedResponse.toDomain(): FeedResult = FeedResult(
    posts = posts,
    nextCursor = nextCursor,
    hasMore = hasMore
)
