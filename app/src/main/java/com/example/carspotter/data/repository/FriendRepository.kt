package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.FriendApi
import com.example.carspotter.data.remote.model.friend.FriendDTO
import com.example.carspotter.domain.repository.IFriendRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepository @Inject constructor(
    private val friendApi: FriendApi
) : BaseRepository(), IFriendRepository {

    override suspend fun getAllFriends(): ApiResult<List<FriendDTO>> {
        return safeApiCall { friendApi.getAllFriends()}
    }

    override suspend fun getAllFriendsAdmin(): ApiResult<List<FriendDTO>> {
        return safeApiCall { friendApi.getAllFriendsAdmin()}
    }

    override suspend fun addFriend(friendId: Int): ApiResult<Unit> {
        return safeApiCall { friendApi.addFriend(friendId)}
    }

    override suspend fun deleteFriend(friendId: Int): ApiResult<Unit> {
        return safeApiCall { friendApi.deleteFriend(friendId)}
    }
}
