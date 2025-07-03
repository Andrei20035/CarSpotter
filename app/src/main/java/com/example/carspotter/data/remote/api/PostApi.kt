package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.post.PostDTO
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface PostApi {

    @POST("posts")
    suspend fun createPost(
        @Body postRequest: PostRequest
    ): Response<Unit>

    @GET("posts/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Int
    ): Response<PostDTO>

    @GET("posts")
    suspend fun getAllPosts(): Response<List<PostDTO>>

    @GET("posts/current-day")
    suspend fun getCurrentDayPostsForUser(
        @Header("Time-Zone") timeZone: String = TimeZone.getDefault().id
    ): Response<List<PostDTO>>

    @PUT("posts/{postId}")
    suspend fun editPost(
        @Path("postId") postId: Int,
        @Body request: PostEditRequest
    ): Response<Unit>

    @DELETE("posts/{postId}")
    suspend fun deletePost(
        @Path("postId") postId: Int
    ): Response<Unit>
}
