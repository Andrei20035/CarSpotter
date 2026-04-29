package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserApi
import com.example.carspotter.data.remote.dto.user.*
import com.example.carspotter.data.model.User
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    suspend fun getCurrentUser(): ApiResult<User>
    suspend fun getAllUsers(): ApiResult<List<User>>
    suspend fun getUsersByUsername(username: String): ApiResult<List<User>>
    suspend fun createUser(request: CreateUserRequest): ApiResult<CreateUserResponse>
    suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): ApiResult<Unit>
    suspend fun deleteCurrentUser(): ApiResult<Unit>
}
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun getCurrentUser(): ApiResult<User> {
        return when (val result = safeApiCall { userApi.getCurrentUser()}) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getAllUsers(): ApiResult<List<User>> {
        return when (val result = safeApiCall { userApi.getAllUsers()}) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getUsersByUsername(username: String): ApiResult<List<User>> {
        return when (val result = safeApiCall { userApi.getUsersByUsername(username)}) {
            is ApiResult.Success -> ApiResult.Success(result.data)
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
