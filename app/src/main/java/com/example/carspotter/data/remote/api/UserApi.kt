package com.example.carspotter.data.remote.api

import com.example.carspotter.data.model.User
import com.example.carspotter.data.remote.dto.user.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("user/me")
    suspend fun getCurrentUser(): Response<User>

    @GET("user/all")
    suspend fun getAllUsers(): Response<List<User>>

    @GET("user/by-username/{username}")
    suspend fun getUsersByUsername(
        @Path("username") username: String
    ): Response<List<User>>

    @POST("user")
    suspend fun createUser(
        @Body createUserRequest: CreateUserRequest
    ): Response<CreateUserResponse>

    @PUT("user/profile-picture")
    suspend fun updateProfilePicture(
        @Body updateProfilePictureRequest: UpdateProfilePictureRequest
    ): Response<Unit>

    @DELETE("user/me")
    suspend fun deleteCurrentUser(): Response<Unit>
}