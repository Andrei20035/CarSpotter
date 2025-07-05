package com.example.carspotter.domain.model

import com.example.carspotter.data.remote.model.friend.FriendDTO
import java.time.Instant
import java.util.UUID

data class Friend(
    val userId: UUID,
    val friendId: UUID,
    val createdAt: Instant? = null
)

fun FriendDTO.toDomain(): Friend = Friend(
    userId = userId,
    friendId = friendId,
    createdAt = createdAt
)

fun List<FriendDTO>.toDomain(): List<Friend> {
    return map { it.toDomain() }
}