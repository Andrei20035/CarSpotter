package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.friend_request.FriendRequestDTO
import com.example.carspotter.utils.ApiResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FriendRequestApi {

    @GET("friend-requests/admin")
    suspend fun getAllFriendRequestsAdmin(): ApiResult<List<FriendRequestDTO>>

    @POST("friend-requests/{receiverId}")
    suspend fun sendFriendRequest(
        @Path("receiverId") receiverId: Int
    ): ApiResult<Unit>

    @POST("friend-requests/{senderId}/accept")
    suspend fun acceptFriendRequest(
        @Path("senderId") senderId: Int
    ): ApiResult<Unit>

    @POST("friend-requests/{senderId}/decline")
    suspend fun declineFriendRequest(
        @Path("senderId") senderId: Int
    ): ApiResult<Unit>

    @GET("friend-requests")
    suspend fun getAllFriendRequests(): ApiResult<List<FriendRequestDTO>>
}
