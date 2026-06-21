package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.PostApi
import com.example.carspotter.data.remote.dto.post.CreatePostMetadata
import com.example.carspotter.data.remote.dto.post.FeedCursor
import com.example.carspotter.data.remote.dto.post.FeedResult
import com.example.carspotter.data.remote.dto.post.PostEditRequest
import com.example.carspotter.data.remote.dto.post.toDomain
import com.example.carspotter.data.model.Post
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface PostRepository {
    /** Creates a post via multipart upload (JSON metadata part + image bytes part). */
    suspend fun createPost(
        metadata: CreatePostMetadata,
        imageBytes: ByteArray,
        mimeType: String,
    ): ApiResult<Unit>
    suspend fun getPostById(postId: UUID): ApiResult<Post>
    suspend fun getAllPosts(): ApiResult<List<Post>>
    suspend fun getCurrentDayPostsForUser(): ApiResult<List<Post>>
    suspend fun editPost(postId: UUID, request: PostEditRequest): ApiResult<Unit>
    suspend fun deletePost(postId: UUID): ApiResult<Unit>
    suspend fun getFeedPosts(limit: Int, cursor: FeedCursor? = null): ApiResult<FeedResult>
}
@Singleton
class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi
) : PostRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun createPost(
        metadata: CreatePostMetadata,
        imageBytes: ByteArray,
        mimeType: String,
    ): ApiResult<Unit> {
        val metadataPart = json.encodeToString(metadata)
            .toRequestBody("application/json".toMediaType())
        val imagePart = MultipartBody.Part.createFormData(
            name = "image",
            filename = "post.jpg",
            body = imageBytes.toRequestBody(mimeType.toMediaTypeOrNull()),
        )
        return when (val result = safeApiCall { postApi.createPost(metadataPart, imagePart) }) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
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

    override suspend fun getFeedPosts(limit: Int, cursor: FeedCursor?): ApiResult<FeedResult> {
        return when (
            val result = safeApiCall {
                postApi.getFeedPosts(
                    limit = limit,
                    cursorCreatedAt = cursor?.lastCreatedAt?.toString(),
                    cursorPostId = cursor?.lastPostId?.toString()
                )
            }
        ) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
