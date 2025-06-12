package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.utils.ApiResult

interface ILikeRepository {
    suspend fun likePost(postId: Int): ApiResult<Unit>
    suspend fun unlikePost(postId: Int): ApiResult<Unit>
    suspend fun getLikesForPost(postId: Int): ApiResult<List<UserDTO>>
}