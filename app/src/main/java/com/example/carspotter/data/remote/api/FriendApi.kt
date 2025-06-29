package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.friend.FriendDTO
import com.example.carspotter.utils.ApiResult
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendApi {

    @GET("friends/admin")
    suspend fun getAllFriendsAdmin(): ApiResult<List<FriendDTO>>

    @POST("friends/{friendId}")
    suspend fun addFriend(
        @Path("friendId") friendId: Int
    ): ApiResult<Unit>

    @DELETE("friends/{friendId}")
    suspend fun deleteFriend(
        @Path("friendId") friendId: Int
    ): ApiResult<Unit>

    @GET("friends")
    suspend fun getAllFriends(): ApiResult<List<FriendDTO>>
}
