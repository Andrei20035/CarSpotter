package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.LikeApi
import com.example.carspotter.data.remote.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikeRepository @Inject constructor(
    private val likeApi: LikeApi
) : BaseRepository() {

    suspend fun likePost(postId: Int): ApiResult<Unit> {
        return safeApiCall { likeApi.likePost(postId) }
    }

    suspend fun unlikePost(postId: Int): ApiResult<Unit> {
        return safeApiCall { likeApi.unlikePost(postId) }
    }

    suspend fun getLikesForPost(postId: Int): ApiResult<List<User>> {
        return safeApiCall { likeApi.getLikesForPost(postId) }
    }
}
