package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserApi
import com.example.carspotter.data.remote.model.user.CreateUserRequest
import com.example.carspotter.data.remote.model.user.CreateUserResponse
import com.example.carspotter.data.remote.model.user.UpdateProfilePictureRequest
import com.example.carspotter.data.remote.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi
) : BaseRepository() {

    suspend fun getCurrentUser(): ApiResult<User> {
        return safeApiCall { userApi.getCurrentUser() }
    }

    suspend fun getAllUsers(): ApiResult<List<User>> {
        return safeApiCall { userApi.getAllUsers() }
    }

    suspend fun getUsersByUsername(username: String): ApiResult<List<User>> {
        return safeApiCall { userApi.getUsersByUsername(username) }
    }

    suspend fun createUser(request: CreateUserRequest): ApiResult<CreateUserResponse> {
        return safeApiCall { userApi.createUser(request) }
    }

    suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): ApiResult<Unit> {
        return safeApiCall { userApi.updateProfilePicture(request) }
    }

    suspend fun deleteCurrentUser(): ApiResult<Unit> {
        return safeApiCall { userApi.deleteCurrentUser() }
    }
}
