package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.domain.model.User
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface ILikeRepository {
    suspend fun likePost(postId: UUID): ApiResult<Unit>
    suspend fun unlikePost(postId: UUID): ApiResult<Unit>
    suspend fun getLikesForPost(postId: UUID): ApiResult<List<User>>
}