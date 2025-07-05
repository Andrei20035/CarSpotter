package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.comment.CommentDTO
import com.example.carspotter.data.remote.model.comment.CommentRequest
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface CommentApi {

    @GET("comments/{postId}")
    suspend fun getCommentsForPost(
        @Path("postId") postId: UUID
    ): Response<List<CommentDTO>>

    @POST("comments")
    suspend fun addComment(
        @Body commentRequest: CommentRequest
    ): Response<Unit>

    @DELETE("comments/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: UUID
    ): Response<Unit>
}