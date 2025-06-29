package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.CommentApi
import com.example.carspotter.data.remote.model.comment.CommentDTO
import com.example.carspotter.data.remote.model.comment.CommentRequest
import com.example.carspotter.domain.repository.ICommentRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentApi: CommentApi
) : BaseRepository(), ICommentRepository {

    /**
     * Get all comments for a specific post.
     *
     * @param postId The ID of the post
     * @return ApiResult containing a list of CommentDTO or error
     */
    override suspend fun getCommentsForPost(postId: Int): ApiResult<List<CommentDTO>> {
        return getCommentsForPost(postId)
    }

    /**
     * Add a new comment.
     *
     * @param commentRequest The comment request body
     * @return ApiResult containing Unit on success or error
     */
    override suspend fun addComment(commentRequest: CommentRequest): ApiResult<Unit> {
        return addComment(commentRequest)
    }

    /**
     * Delete a comment by ID.
     *
     * @param commentId The ID of the comment to delete
     * @return ApiResult containing Unit on success or error
     */
    override suspend fun deleteComment(commentId: Int): ApiResult<Unit> {
        return deleteComment(commentId)
    }
}
