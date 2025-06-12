package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserApi
import com.example.carspotter.data.remote.model.user.CreateUserRequest
import com.example.carspotter.data.remote.model.user.CreateUserResponse
import com.example.carspotter.data.remote.model.user.UpdateProfilePictureRequest
import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.domain.repository.IUserRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) : BaseRepository(), IUserRepository {

    override suspend fun getCurrentUser(): ApiResult<UserDTO> {
        return safeApiCall { userApi.getCurrentUser() }
    }

    override suspend fun getAllUsers(): ApiResult<List<UserDTO>> {
        return safeApiCall { userApi.getAllUsers() }
    }

    override suspend fun getUsersByUsername(username: String): ApiResult<List<UserDTO>> {
        return safeApiCall { userApi.getUsersByUsername(username) }
    }

    override suspend fun createUser(request: CreateUserRequest): ApiResult<CreateUserResponse> {
        return safeApiCall { userApi.createUser(request) }
    }

    override suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): ApiResult<Unit> {
        return safeApiCall { userApi.updateProfilePicture(request) }
    }

    override suspend fun deleteCurrentUser(): ApiResult<Unit> {
        return safeApiCall { userApi.deleteCurrentUser() }
    }
}
