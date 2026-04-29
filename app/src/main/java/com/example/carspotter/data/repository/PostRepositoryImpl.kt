package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.PostApi
import com.example.carspotter.data.remote.dto.post.FeedRequest
import com.example.carspotter.data.remote.dto.post.FeedResult
import com.example.carspotter.data.remote.dto.post.PostEditRequest
import com.example.carspotter.data.remote.dto.post.PostRequest
import com.example.carspotter.data.remote.dto.post.toDomain
import com.example.carspotter.data.model.Post
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface PostRepository {
    suspend fun createPost(postRequest: PostRequest): ApiResult<Unit>
    suspend fun getPostById(postId: UUID): ApiResult<Post>
    suspend fun getAllPosts(): ApiResult<List<Post>>
    suspend fun getCurrentDayPostsForUser(): ApiResult<List<Post>>
    suspend fun editPost(postId: UUID, request: PostEditRequest): ApiResult<Unit>
    suspend fun deletePost(postId: UUID): ApiResult<Unit>
    suspend fun getFeedPosts(feedRequest: FeedRequest): ApiResult<FeedResult>
}
@Singleton
class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi
) : PostRepository {

    override suspend fun createPost(postRequest: PostRequest): ApiResult<Unit> {
        return safeApiCall { postApi.createPost(postRequest) }
    }

    override suspend fun getPostById(postId: UUID): ApiResult<Post> {
        return when (val result = safeApiCall { postApi.getPostById(postId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getAllPosts(): ApiResult<List<Post>> {
        return when (val result = safeApiCall { postApi.getAllPosts()}) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getCurrentDayPostsForUser(): ApiResult<List<Post>> {
        val timeZone = TimeZone.getDefault().id
        return when (val result = safeApiCall { postApi.getCurrentDayPostsForUser(timeZone) }) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun editPost(postId: UUID, request: PostEditRequest): ApiResult<Unit> {
        return safeApiCall { postApi.editPost(postId, request)}
    }

    override suspend fun deletePost(postId: UUID): ApiResult<Unit> {
        return safeApiCall { postApi.deletePost(postId)}
    }

    override suspend fun getFeedPosts(feedRequest: FeedRequest): ApiResult<FeedResult> {
        return when(val result = safeApiCall { postApi.getFeedPosts(feedRequest) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
