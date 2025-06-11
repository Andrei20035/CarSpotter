package com.example.carspotter.data.remote.repository

import com.example.carspotter.data.remote.api.FriendApi
import com.example.carspotter.data.remote.model.friend.Friend
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRepository @Inject constructor(
    private val friendApi: FriendApi
) : BaseRepository() {

    suspend fun getAllFriends(): ApiResult<List<Friend>> {
        return safeApiCall { friendApi.getAllFriends() }
    }

    suspend fun getAllFriendsAdmin(): ApiResult<List<Friend>> {
        return safeApiCall { friendApi.getAllFriendsAdmin() }
    }

    suspend fun addFriend(friendId: Int): ApiResult<Unit> {
        return safeApiCall { friendApi.addFriend(friendId) }
    }

    suspend fun deleteFriend(friendId: Int): ApiResult<Unit> {
        return safeApiCall { friendApi.deleteFriend(friendId) }
    }
}
