package com.example.carspotter.data.model

import java.time.Instant
import java.util.UUID

/**
 * Domain model for a single feed item. Decoupled from the network DTO so the UI layer
 * does not depend on serialization concerns.
 */
data class FeedPost(
    val id: UUID,
    val userId: UUID,
    val username: String,
    val brand: String,
    val model: String,
    val imageUrl: String,
    val caption: String?,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: Instant,
    val likeCount: Long,
    val commentCount: Long,
    val likedByCurrentUser: Boolean,
) {
    /** e.g. "Porsche 911" — used as the headline car label in the feed card. */
    val carName: String get() = "$brand $model"
}
