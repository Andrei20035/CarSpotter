package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.comment.CommentDTO
import com.example.carspotter.data.remote.model.comment.CommentRequest
import com.example.carspotter.utils.ApiResult
import retrofit2.http.*

interface CommentApi {

    @GET("comments/{postId}")
    suspend fun getCommentsForPost(
        @Path("postId") postId: Int
    ): ApiResult<List<CommentDTO>>

    @POST("comments")
    suspend fun addComment(
        @Body commentRequest: CommentRequest
    ): ApiResult<Unit>

    @DELETE("comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Int
    ): ApiResult<Unit>
}