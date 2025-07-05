package com.example.carspotter.domain.model

import com.example.carspotter.data.remote.model.friend_request.FriendRequestDTO
import java.time.Instant
import java.util.UUID

data class FriendRequest(
    val senderId: UUID,
    val receiverId: UUID,
    val createdAt: Instant? = null,
)

fun FriendRequestDTO.toDomain(): FriendRequest = FriendRequest(
    senderId = senderId,
    receiverId = receiverId,
    createdAt = createdAt
)

fun List<FriendRequestDTO>.toDomain(): List<FriendRequest> {
    return map { it.toDomain() }
}