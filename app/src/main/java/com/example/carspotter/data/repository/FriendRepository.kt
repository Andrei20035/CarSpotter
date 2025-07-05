package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.FriendApi
import com.example.carspotter.data.remote.model.friend.FriendDTO
import com.example.carspotter.domain.model.Friend
import com.example.carspotter.domain.model.toDomain
import com.example.carspotter.domain.repository.IFriendRepository
import com.example.carspotter.utils.ApiResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepository @Inject constructor(
    private val friendApi: FriendApi
) : BaseRepository(), IFriendRepository {

    override suspend fun getAllFriends(): ApiResult<List<Friend>> {
        return when (val result = safeApiCall { friendApi.getAllFriends()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getAllFriendsAdmin(): ApiResult<List<Friend>> {
        return when (val result = safeApiCall { friendApi.getAllFriendsAdmin()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun addFriend(friendId: UUID): ApiResult<Unit> {
        return safeApiCall { friendApi.addFriend(friendId)}
    }

    override suspend fun deleteFriend(friendId: UUID): ApiResult<Unit> {
        return safeApiCall { friendApi.deleteFriend(friendId)}
    }
}
