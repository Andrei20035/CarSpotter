package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.post.PostDTO
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import com.example.carspotter.utils.ApiResult
import retrofit2.http.*
import java.util.*

interface PostApi {

    @POST("posts")
    suspend fun createPost(
        @Body postRequest: PostRequest
    ): ApiResult<Unit>

    @GET("posts/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Int
    ): ApiResult<PostDTO>

    @GET("posts")
    suspend fun getAllPosts(): ApiResult<List<PostDTO>>

    @GET("posts/current-day")
    suspend fun getCurrentDayPostsForUser(
        @Header("Time-Zone") timeZone: String = TimeZone.getDefault().id
    ): ApiResult<List<PostDTO>>

    @PUT("posts/{postId}")
    suspend fun editPost(
        @Path("postId") postId: Int,
        @Body request: PostEditRequest
    ): ApiResult<Unit>

    @DELETE("posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ): ApiResult<Unit>
}
