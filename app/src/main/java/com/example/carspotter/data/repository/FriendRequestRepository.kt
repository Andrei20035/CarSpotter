package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.FriendRequestApi
import com.example.carspotter.data.remote.model.friend_request.FriendRequestDTO
import com.example.carspotter.domain.model.FriendRequest
import com.example.carspotter.domain.model.toDomain
import com.example.carspotter.domain.repository.IFriendRequestRepository
import com.example.carspotter.utils.ApiResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRequestRepository @Inject constructor(
    private val friendRequestApi: FriendRequestApi
) : BaseRepository(), IFriendRequestRepository {

    override suspend fun getAllFriendRequests(): ApiResult<List<FriendRequest>> {
        return when (val result = safeApiCall { friendRequestApi.getAllFriendRequests()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getAllFriendRequestsAdmin(): ApiResult<List<FriendRequest>> {
        return when (val result = safeApiCall { friendRequestApi.getAllFriendRequestsAdmin()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun sendFriendRequest(receiverId: UUID): ApiResult<Unit> {
        return safeApiCall { friendRequestApi.sendFriendRequest(receiverId)}
    }

    override suspend fun acceptFriendRequest(senderId: UUID): ApiResult<Unit> {
        return safeApiCall { friendRequestApi.acceptFriendRequest(senderId)}
    }

    override suspend fun declineFriendRequest(senderId: UUID): ApiResult<Unit> {
        return safeApiCall { friendRequestApi.declineFriendRequest(senderId)}
    }
}
