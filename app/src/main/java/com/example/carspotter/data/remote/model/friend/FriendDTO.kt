package com.example.carspotter.data.remote.model.friend

import com.example.carspotter.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class FriendDTO(
    val userId: Int,
    val friendId: Int,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
)