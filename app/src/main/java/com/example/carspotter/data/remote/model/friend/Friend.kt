package com.example.carspotter.data.remote.model.friend

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Friend(
    val userId: Int,
    val friendId: Int,
    val createdAt: Instant? = null,
)