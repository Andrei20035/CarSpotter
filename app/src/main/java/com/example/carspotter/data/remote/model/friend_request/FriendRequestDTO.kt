package com.example.carspotter.data.remote.model.friend_request

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class FriendRequestDTO(
    val senderId: Int,
    val receiverId: Int,
    val createdAt: Instant? = null,
)
