package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.CommentApi
import com.example.carspotter.data.remote.dto.comment.CommentRequest
import com.example.carspotter.data.model.Comment
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface CommentRepository {
    suspend fun getCommentsForPost(postId: UUID): ApiResult<List<Comment>>
    suspend fun addComment(commentRequest: CommentRequest): ApiResult<Unit>
    suspend fun deleteComment(commentId: UUID): ApiResult<Unit>
}

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val commentApi: CommentApi
) : CommentRepository {

    /**
     * Get all comments for a specific post.
     *
     * @param postId The ID of the post
     * @return ApiResult containing a list of CommentDTO or error
     */
    override suspend fun getCommentsForPost(postId: UUID): ApiResult<List<Comment>> {
        return when (val result = safeApiCall { commentApi.getCommentsForPost(postId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    /**
     * Add a new comment.
     *
     * @param commentRequest The comment request body
     * @return ApiResult containing Unit on success or error
     */
    override suspend fun addComment(commentRequest: CommentRequest): ApiResult<Unit> {
        return safeApiCall { commentApi.addComment(commentRequest) }
    }

    /**
     * Delete a comment by ID.
     *
     * @param commentId The ID of the comment to delete
     * @return ApiResult containing Unit on success or error
     */
    override suspend fun deleteComment(commentId: UUID): ApiResult<Unit> {
        return safeApiCall { commentApi.deleteComment(commentId) }
    }
}
