package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.UserDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface LikeApi {

    @POST("likes/{postId}")
    suspend fun likePost(
        @Path("postId") postId: UUID
    ): Response<Unit>

    @DELETE("likes/{postId}")
    suspend fun unlikePost(
        @Path("postId") postId: UUID
    ): Response<Unit>

    @GET("likes/posts/{postId}")
    suspend fun getLikesForPost(
        @Path("postId") postId: UUID
    ): Response<List<UserDTO>>
}
