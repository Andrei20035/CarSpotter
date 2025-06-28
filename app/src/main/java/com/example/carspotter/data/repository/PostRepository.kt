package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.PostApi
import com.example.carspotter.data.remote.model.post.PostDTO
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import com.example.carspotter.domain.repository.IPostRepository
import com.example.carspotter.utils.ApiResult
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val postApi: PostApi
) : BaseRepository(), IPostRepository {

    override suspend fun createPost(postRequest: PostRequest): ApiResult<Unit> {
        return safeApiCall { postApi.createPost(postRequest) }
    }

    override suspend fun getPostById(postId: Int): ApiResult<PostDTO> {
        return safeApiCall { postApi.getPostById(postId) }
    }

    override suspend fun getAllPosts(): ApiResult<List<PostDTO>> {
        return safeApiCall { postApi.getAllPosts() }
    }

    override suspend fun getCurrentDayPostsForUser(): ApiResult<List<PostDTO>> {
        val timeZone = TimeZone.getDefault().id
        return safeApiCall { postApi.getCurrentDayPostsForUser(timeZone) }
    }

    override suspend fun editPost(postId: Int, request: PostEditRequest): ApiResult<Unit> {
        return safeApiCall { postApi.editPost(postId, request) }
    }

    override suspend fun deletePost(postId: Int): ApiResult<Unit> {
        return safeApiCall { postApi.deletePost(postId) }
    }
}
