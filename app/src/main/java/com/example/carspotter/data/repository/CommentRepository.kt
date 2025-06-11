package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.CommentApi
import com.example.carspotter.data.remote.model.comment.Comment
import com.example.carspotter.data.remote.model.comment.CommentRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentApi: CommentApi
) : BaseRepository() {

    /**
     * Get all comments for a specific post.
     *
     * @param postId The ID of the post
     * @return ApiResult containing a list of Comment or error
     */
    suspend fun getCommentsForPost(postId: Int): ApiResult<List<Comment>> {
        return safeApiCall { commentApi.getCommentsForPost(postId) }
    }

    /**
     * Add a new comment.
     *
     * @param commentRequest The comment request body
     * @return ApiResult containing Unit on success or error
     */
    suspend fun addComment(commentRequest: CommentRequest): ApiResult<Unit> {
        return safeApiCall { commentApi.addComment(commentRequest) }
    }

    /**
     * Delete a comment by ID.
     *
     * @param commentId The ID of the comment to delete
     * @return ApiResult containing Unit on success or error
     */
    suspend fun deleteComment(commentId: Int): ApiResult<Unit> {
        return safeApiCall { commentApi.deleteComment(commentId) }
    }
}
