package com.example.carspotter.data.remote.api

import com.example.carspotter.data.model.User
import com.example.carspotter.data.model.UserCar
import com.example.carspotter.data.remote.dto.user_car.UserCarRequest
import com.example.carspotter.data.remote.dto.user_car.UserCarUpdateRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.UUID

interface UserCarApi {

    @Multipart
    @POST("me/car")
    suspend fun createMyCar(
        @Part("metadata") metadata: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<UserCar>

    @GET("user-cars/{userCarId}")
    suspend fun getUserCarById(
        @Path("userCarId") userCarId: UUID
    ): Response<UserCar>

    @GET("user-cars/by-user/{userId}")
    suspend fun getUserCarByUserId(
        @Path("userId") userId: UUID
    ): Response<UserCar>

    @GET("user-cars/{userCarId}/user")
    suspend fun getUserByUserCarId(
        @Path("userCarId") userCarId: UUID
    ): Response<User>

    @Multipart
    @PATCH("me/car")
    suspend fun updateMyCar(
        @Part("metadata") metadata: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<UserCar>

    @DELETE("user-cars")
    suspend fun deleteUserCar(): Response<Unit>

    @GET("user-cars")
    suspend fun getAllUserCars(): Response<List<UserCar>>
}
