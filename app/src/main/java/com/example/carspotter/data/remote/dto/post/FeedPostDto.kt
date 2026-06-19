package com.example.carspotter.data.remote.dto.post

import com.example.carspotter.core.network.serialization.InstantSerializer
import com.example.carspotter.core.network.serialization.UUIDSerializer
import com.example.carspotter.data.model.FeedPost
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

/**
 * Network shape of a feed item. Mirrors the server `PostDTO` returned by `GET /posts/feed`.
 * Kept separate from the generic [com.example.carspotter.data.model.Post] because the feed
 * response carries denormalized author/car fields and engagement counters.
 */
@Serializable
data class FeedPostDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val username: String,
    @Serializable(with = UUIDSerializer::class)
    val carModelId: UUID? = null,
    val brand: String,
    val model: String,
    val imageUrl: String,
    val caption: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    val likeCount: Long = 0,
    val commentCount: Long = 0,
    val likedByCurrentUser: Boolean = false,
)

fun FeedPostDto.toDomain(): FeedPost = FeedPost(
    id = id,
    userId = userId,
    username = username,
    brand = brand,
    model = model,
    imageUrl = imageUrl,
    caption = caption,
    latitude = latitude,
    longitude = longitude,
    createdAt = createdAt,
    likeCount = likeCount,
    commentCount = commentCount,
    likedByCurrentUser = likedByCurrentUser,
)
