package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.comment.CommentDTO
import com.example.carspotter.data.remote.model.comment.CommentRequest
import com.example.carspotter.domain.model.Comment
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface ICommentRepository {
    suspend fun getCommentsForPost(postId: UUID): ApiResult<List<Comment>>
    suspend fun addComment(commentRequest: CommentRequest): ApiResult<Unit>
    suspend fun deleteComment(commentId: UUID): ApiResult<Unit>
}