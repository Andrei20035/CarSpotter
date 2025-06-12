package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.friend.FriendDTO
import com.example.carspotter.utils.ApiResult

interface IFriendRepository {
    suspend fun getAllFriends(): ApiResult<List<FriendDTO>>
    suspend fun getAllFriendsAdmin(): ApiResult<List<FriendDTO>>
    suspend fun addFriend(friendId: Int): ApiResult<Unit>
    suspend fun deleteFriend(friendId: Int): ApiResult<Unit>
}