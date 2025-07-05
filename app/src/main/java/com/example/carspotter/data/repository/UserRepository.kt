package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserApi
import com.example.carspotter.data.remote.model.user.*
import com.example.carspotter.domain.model.User
import com.example.carspotter.domain.model.toDomain
import com.example.carspotter.domain.repository.IUserRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) : BaseRepository(), IUserRepository {

    override suspend fun getCurrentUser(): ApiResult<User> {
        return when (val result = safeApiCall { userApi.getCurrentUser()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getAllUsers(): ApiResult<List<User>> {
        return when (val result = safeApiCall { userApi.getAllUsers()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getUsersByUsername(username: String): ApiResult<List<User>> {
        return when (val result = safeApiCall { userApi.getUsersByUsername(username)}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun createUser(request: CreateUserRequest): ApiResult<CreateUserResponse> {
        return safeApiCall { userApi.createUser(request)}
    }

    override suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): ApiResult<Unit> {
        return safeApiCall { userApi.updateProfilePicture(request)}
    }

    override suspend fun deleteCurrentUser(): ApiResult<Unit> {
        return safeApiCall { userApi.deleteCurrentUser()}
    }
}
