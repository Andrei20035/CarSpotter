package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import com.example.carspotter.data.model.LikeStatus
import com.example.carspotter.data.remote.api.LikeApi
import com.example.carspotter.data.remote.dto.like.toDomain
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface LikeRepository {
    /** Toggle the current user's like on [postId]; returns the authoritative state on success. */
    suspend fun toggleLike(postId: UUID): ApiResult<LikeStatus>

    /** Current like state for [postId]. */
    suspend fun getLikeStatus(postId: UUID): ApiResult<LikeStatus>
}

@Singleton
class LikeRepositoryImpl @Inject constructor(
    private val likeApi: LikeApi
) : LikeRepository {

    override suspend fun toggleLike(postId: UUID): ApiResult<LikeStatus> {
        return when (val result = safeApiCall { likeApi.toggleLike(postId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getLikeStatus(postId: UUID): ApiResult<LikeStatus> {
        return when (val result = safeApiCall { likeApi.getLikeStatus(postId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
