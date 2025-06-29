package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.utils.ApiResult
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeApi {

    @POST("likes/{postId}")
    suspend fun likePost(
        @Path("postId") postId: Int
    ): ApiResult<Unit>

    @DELETE("likes/{postId}")
    suspend fun unlikePost(
        @Path("postId") postId: Int
    ): ApiResult<Unit>

    @GET("likes/posts/{postId}")
    suspend fun getLikesForPost(
        @Path("postId") postId: Int
    ): ApiResult<List<UserDTO>>
}
