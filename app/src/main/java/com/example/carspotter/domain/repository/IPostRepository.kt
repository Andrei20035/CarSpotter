package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.post.PostDTO
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import com.example.carspotter.domain.model.Post
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface IPostRepository {
    suspend fun createPost(postRequest: PostRequest): ApiResult<Unit>
    suspend fun getPostById(postId: UUID): ApiResult<Post>
    suspend fun getAllPosts(): ApiResult<List<Post>>
    suspend fun getCurrentDayPostsForUser(): ApiResult<List<Post>>
    suspend fun editPost(postId: UUID, request: PostEditRequest): ApiResult<Unit>
    suspend fun deletePost(postId: UUID): ApiResult<Unit>
}