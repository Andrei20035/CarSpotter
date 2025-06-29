package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.LikeApi
import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.domain.repository.ILikeRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LikeRepository @Inject constructor(
    private val likeApi: LikeApi
) : BaseRepository(), ILikeRepository {

    override suspend fun likePost(postId: Int): ApiResult<Unit> {
        return likeApi.likePost(postId)
    }

    override suspend fun unlikePost(postId: Int): ApiResult<Unit> {
        return likeApi.unlikePost(postId)
    }

    override suspend fun getLikesForPost(postId: Int): ApiResult<List<UserDTO>> {
        return likeApi.getLikesForPost(postId)
    }
}
