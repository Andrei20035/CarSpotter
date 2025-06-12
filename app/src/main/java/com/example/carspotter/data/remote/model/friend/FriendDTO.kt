package com.example.carspotter.data.remote.model.friend

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class FriendDTO(
    val userId: Int,
    val friendId: Int,
    val createdAt: Instant? = null,
)