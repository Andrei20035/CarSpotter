package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.CreateUserRequest
import com.example.carspotter.data.remote.model.user.CreateUserResponse
import com.example.carspotter.data.remote.model.user.UpdateProfilePictureRequest
import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.utils.ApiResult

interface IUserRepository {
    suspend fun getCurrentUser(): ApiResult<UserDTO>
    suspend fun getAllUsers(): ApiResult<List<UserDTO>>
    suspend fun getUsersByUsername(username: String): ApiResult<List<UserDTO>>
    suspend fun createUser(request: CreateUserRequest): ApiResult<CreateUserResponse>
    suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): ApiResult<Unit>
    suspend fun deleteCurrentUser(): ApiResult<Unit>
}