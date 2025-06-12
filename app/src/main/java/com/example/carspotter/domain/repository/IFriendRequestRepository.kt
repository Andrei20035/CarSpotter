package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.friend_request.FriendRequestDTO
import com.example.carspotter.utils.ApiResult

interface IFriendRequestRepository {
    suspend fun getAllFriendRequests(): ApiResult<List<FriendRequestDTO>>
    suspend fun getAllFriendRequestsAdmin(): ApiResult<List<FriendRequestDTO>>
    suspend fun sendFriendRequest(receiverId: Int): ApiResult<Unit>
    suspend fun acceptFriendRequest(senderId: Int): ApiResult<Unit>
    suspend fun declineFriendRequest(senderId: Int): ApiResult<Unit>
}