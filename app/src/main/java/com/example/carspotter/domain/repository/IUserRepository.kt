package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.*
import com.example.carspotter.domain.model.User
import com.example.carspotter.utils.ApiResult

interface IUserRepository {
    suspend fun getCurrentUser(): ApiResult<User>
    suspend fun getAllUsers(): ApiResult<List<User>>
    suspend fun getUsersByUsername(username: String): ApiResult<List<User>>
    suspend fun createUser(request: CreateUserRequest): ApiResult<CreateUserResponse>
    suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): ApiResult<Unit>
    suspend fun deleteCurrentUser(): ApiResult<Unit>
}