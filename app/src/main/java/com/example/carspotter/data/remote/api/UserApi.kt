package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.*
import com.example.carspotter.utils.ApiResult
import retrofit2.http.*

interface UserApi {

    @GET("user/me")
    suspend fun getCurrentUser(): ApiResult<UserDTO>

    @GET("user/all")
    suspend fun getAllUsers(): ApiResult<List<UserDTO>>

    @GET("user/by-username/{username}")
    suspend fun getUsersByUsername(
        @Path("username") username: String
    ): ApiResult<List<UserDTO>>

    @POST("user")
    suspend fun createUser(
        @Body createUserRequest: CreateUserRequest
    ): ApiResult<CreateUserResponse>

    @PUT("user/profile-picture")
    suspend fun updateProfilePicture(
        @Body updateProfilePictureRequest: UpdateProfilePictureRequest
    ): ApiResult<Unit>

    @DELETE("user/me")
    suspend fun deleteCurrentUser(): ApiResult<Unit>

    @POST("upload-url")
    suspend fun getUploadUrl(
        @Body uploadImageRequest: UploadImageRequest
    ): ApiResult<UploadUrlResponse>
}