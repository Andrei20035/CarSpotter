package com.example.carspotter.data.remote.repository

import com.example.carspotter.data.remote.api.FriendRequestApi
import com.example.carspotter.data.remote.model.friend_request.FriendRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRequestRepository @Inject constructor(
    private val friendRequestApi: FriendRequestApi
) : BaseRepository() {

    suspend fun getAllFriendRequests(): ApiResult<List<FriendRequest>> {
        return safeApiCall { friendRequestApi.getAllFriendRequests() }
    }

    suspend fun getAllFriendRequestsAdmin(): ApiResult<List<FriendRequest>> {
        return safeApiCall { friendRequestApi.getAllFriendRequestsAdmin() }
    }

    suspend fun sendFriendRequest(receiverId: Int): ApiResult<Unit> {
        return safeApiCall { friendRequestApi.sendFriendRequest(receiverId) }
    }

    suspend fun acceptFriendRequest(senderId: Int): ApiResult<Unit> {
        return safeApiCall { friendRequestApi.acceptFriendRequest(senderId) }
    }

    suspend fun declineFriendRequest(senderId: Int): ApiResult<Unit> {
        return safeApiCall { friendRequestApi.declineFriendRequest(senderId) }
    }
}
