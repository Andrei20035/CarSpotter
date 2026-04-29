package com.example.carspotter.data.model

import com.example.carspotter.core.network.serialization.InstantSerializer
import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Friend(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val friendId: UUID,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null
)
