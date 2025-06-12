package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.comment.CommentDTO
import com.example.carspotter.data.remote.model.comment.CommentRequest
import com.example.carspotter.utils.ApiResult

interface ICommentRepository {
    suspend fun getCommentsForPost(postId: Int): ApiResult<List<CommentDTO>>
    suspend fun addComment(commentRequest: CommentRequest): ApiResult<Unit>
    suspend fun deleteComment(commentId: Int): ApiResult<Unit>
}