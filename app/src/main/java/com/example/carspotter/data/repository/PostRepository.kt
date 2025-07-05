package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.PostApi
import com.example.carspotter.data.remote.model.post.PostDTO
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import com.example.carspotter.domain.model.Post
import com.example.carspotter.domain.model.toDomain
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

    override suspend fun getPostById(postId: UUID): ApiResult<Post> {
        return when (val result = safeApiCall { postApi.getPostById(postId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getAllPosts(): ApiResult<List<Post>> {
        return when (val result = safeApiCall { postApi.getAllPosts()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getCurrentDayPostsForUser(): ApiResult<List<Post>> {
        val timeZone = TimeZone.getDefault().id
        return when (val result = safeApiCall { postApi.getCurrentDayPostsForUser(timeZone) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun editPost(postId: UUID, request: PostEditRequest): ApiResult<Unit> {
        return safeApiCall { postApi.editPost(postId, request)}
    }

    override suspend fun deletePost(postId: UUID): ApiResult<Unit> {
        return safeApiCall { postApi.deletePost(postId)}
    }
}
