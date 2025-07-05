package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.LikeApi
import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.domain.model.User
import com.example.carspotter.domain.model.toDomain
import com.example.carspotter.domain.repository.ILikeRepository
import com.example.carspotter.utils.ApiResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikeRepository @Inject constructor(
    private val likeApi: LikeApi
) : BaseRepository(), ILikeRepository {

    override suspend fun likePost(postId: UUID): ApiResult<Unit> {
        return safeApiCall { likeApi.likePost(postId)}
    }

    override suspend fun unlikePost(postId: UUID): ApiResult<Unit> {
        return safeApiCall { likeApi.unlikePost(postId)}
    }

    override suspend fun getLikesForPost(postId: UUID): ApiResult<List<User>> {
        return when (val result = safeApiCall { likeApi.getLikesForPost(postId)}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
