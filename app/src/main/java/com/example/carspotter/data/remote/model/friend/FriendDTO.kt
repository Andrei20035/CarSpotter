package com.example.carspotter.data.remote.model.friend

import com.example.carspotter.serialization.InstantSerializer
import com.example.carspotter.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class FriendDTO(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val friendId: UUID,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
)