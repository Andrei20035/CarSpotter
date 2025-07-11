package com.example.carspotter.data.remote.model.post
import kotlinx.serialization.Serializable


@Serializable
data class FeedRequest(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val radiusKm: Int? = null,
    val limit: Int,
    val cursor: FeedCursor? = null
)
