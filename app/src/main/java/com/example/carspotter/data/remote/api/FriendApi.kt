package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.friend.FriendDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface FriendApi {

    @GET("friends/admin")
    suspend fun getAllFriendsAdmin(): Response<List<FriendDTO>>

    @POST("friends/{friendId}")
    suspend fun addFriend(
        @Path("friendId") friendId: UUID
    ): Response<Unit>

    @DELETE("friends/{friendId}")
    suspend fun deleteFriend(
        @Path("friendId") friendId: UUID
    ): Response<Unit>

    @GET("friends")
    suspend fun getAllFriends(): Response<List<FriendDTO>>
}
