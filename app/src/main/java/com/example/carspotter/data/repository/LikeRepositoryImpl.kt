package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.LikeApi
import com.example.carspotter.data.model.User
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface LikeRepository {
    suspend fun likePost(postId: UUID): ApiResult<Unit>
    suspend fun unlikePost(postId: UUID): ApiResult<Unit>
    suspend fun getLikesForPost(postId: UUID): ApiResult<List<User>>
}
@Singleton
class LikeRepositoryImpl @Inject constructor(
    private val likeApi: LikeApi
) : LikeRepository {

    override suspend fun likePost(postId: UUID): ApiResult<Unit> {
        return safeApiCall { likeApi.likePost(postId)}
    }

    override suspend fun unlikePost(postId: UUID): ApiResult<Unit> {
        return safeApiCall { likeApi.unlikePost(postId)}
    }

    override suspend fun getLikesForPost(postId: UUID): ApiResult<List<User>> {
        return when (val result = safeApiCall { likeApi.getLikesForPost(postId)}) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
