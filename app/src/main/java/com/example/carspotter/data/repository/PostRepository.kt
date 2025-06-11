package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.PostApi
import com.example.carspotter.data.remote.model.post.Post
import com.example.carspotter.data.remote.model.post.PostEditRequest
import com.example.carspotter.data.remote.model.post.PostRequest
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val postApi: PostApi
) : BaseRepository() {

    suspend fun createPost(postRequest: PostRequest): ApiResult<Unit> {
        return safeApiCall { postApi.createPost(postRequest) }
    }

    suspend fun getPostById(postId: Int): ApiResult<Post> {
        return safeApiCall { postApi.getPostById(postId) }
    }

    suspend fun getAllPosts(): ApiResult<List<Post>> {
        return safeApiCall { postApi.getAllPosts() }
    }

    suspend fun getCurrentDayPostsForUser(): ApiResult<List<Post>> {
        val timeZone = TimeZone.getDefault().id
        return safeApiCall { postApi.getCurrentDayPostsForUser(timeZone) }
    }

    suspend fun editPost(postId: Int, request: PostEditRequest): ApiResult<Unit> {
        return safeApiCall { postApi.editPost(postId, request) }
    }

    suspend fun deletePost(postId: Int): ApiResult<Unit> {
        return safeApiCall { postApi.deletePost(postId) }
    }
}
