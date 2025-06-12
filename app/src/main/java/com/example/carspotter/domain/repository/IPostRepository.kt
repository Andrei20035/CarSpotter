package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.post.PostDTO
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import com.example.carspotter.utils.ApiResult

interface IPostRepository {
    suspend fun createPost(postRequest: PostRequest): ApiResult<Unit>
    suspend fun getPostById(postId: Int): ApiResult<PostDTO>
    suspend fun getAllPosts(): ApiResult<List<PostDTO>>
    suspend fun getCurrentDayPostsForUser(): ApiResult<List<PostDTO>>
    suspend fun editPost(postId: Int, request: PostEditRequest): ApiResult<Unit>
    suspend fun deletePost(postId: Int): ApiResult<Unit>
}