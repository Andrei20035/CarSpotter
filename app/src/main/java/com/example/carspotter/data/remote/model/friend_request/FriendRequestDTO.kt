package com.example.carspotter.data.remote.model.friend_request

import com.example.carspotter.serialization.InstantSerializer
import com.example.carspotter.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class FriendRequestDTO(
    @Serializable(with = UUIDSerializer::class)
    val senderId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val receiverId: UUID,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
)
