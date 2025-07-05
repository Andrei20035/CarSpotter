package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.friend.FriendDTO
import com.example.carspotter.domain.model.Friend
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface IFriendRepository {
    suspend fun getAllFriends(): ApiResult<List<Friend>>
    suspend fun getAllFriendsAdmin(): ApiResult<List<Friend>>
    suspend fun addFriend(friendId: UUID): ApiResult<Unit>
    suspend fun deleteFriend(friendId: UUID): ApiResult<Unit>
}