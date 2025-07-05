package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.friend_request.FriendRequestDTO
import com.example.carspotter.domain.model.FriendRequest
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface IFriendRequestRepository {
    suspend fun getAllFriendRequests(): ApiResult<List<FriendRequest>>
    suspend fun getAllFriendRequestsAdmin(): ApiResult<List<FriendRequest>>
    suspend fun sendFriendRequest(receiverId: UUID): ApiResult<Unit>
    suspend fun acceptFriendRequest(senderId: UUID): ApiResult<Unit>
    suspend fun declineFriendRequest(senderId: UUID): ApiResult<Unit>
}