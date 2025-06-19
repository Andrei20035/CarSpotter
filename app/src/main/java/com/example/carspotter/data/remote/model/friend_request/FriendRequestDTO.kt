package com.example.carspotter.data.remote.model.friend_request

import com.example.carspotter.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class FriendRequestDTO(
    val senderId: Int,
    val receiverId: Int,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
)
