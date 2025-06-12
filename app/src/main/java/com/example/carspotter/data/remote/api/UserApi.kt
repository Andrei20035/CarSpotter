package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.CreateUserRequest
import com.example.carspotter.data.remote.model.user.CreateUserResponse
import com.example.carspotter.data.remote.model.user.UpdateProfilePictureRequest
import com.example.carspotter.data.remote.model.user.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("/user/me")
    suspend fun getCurrentUser(): Response<UserDTO>

    @GET("/user/all")
    suspend fun getAllUsers(): Response<List<UserDTO>>

    @GET("/user/by-username/{username}")
    suspend fun getUsersByUsername(
        @Path("username") username: String
    ): Response<List<UserDTO>>

    @POST("/user")
    suspend fun createUser(
        @Body createUserRequest: CreateUserRequest
    ): Response<CreateUserResponse>

    @PUT("/user/profile-picture")
    suspend fun updateProfilePicture(
        @Body updateProfilePictureRequest: UpdateProfilePictureRequest
    ): Response<Unit>

    @DELETE("/user/me")
    suspend fun deleteCurrentUser(): Response<Unit>
}