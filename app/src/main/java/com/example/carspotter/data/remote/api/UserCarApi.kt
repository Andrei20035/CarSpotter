package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.utils.ApiResult
import retrofit2.Response
import retrofit2.http.*

interface UserCarApi {

    @POST("user-cars")
    suspend fun createUserCar(
        @Body userCarRequest: UserCarRequest
    ): Response<Unit>

    @GET("user-cars/{userCarId}")
    suspend fun getUserCarById(
        @Path("userCarId") userCarId: Int
    ): Response<UserCarDTO>

    @GET("user-cars/by-user/{userId}")
    suspend fun getUserCarByUserId(
        @Path("userId") userId: Int
    ): Response<UserCarDTO>

    @GET("user-cars/{userCarId}/user")
    suspend fun getUserByUserCarId(
        @Path("userCarId") userCarId: Int
    ): Response<UserDTO>

    @PUT("user-cars")
    suspend fun updateUserCar(
        @Body userCarUpdateRequest: UserCarUpdateRequest
    ): Response<Unit>

    @DELETE("user-cars")
    suspend fun deleteUserCar(): Response<Unit>

    @GET("user-cars")
    suspend fun getAllUserCars(): Response<List<UserCarDTO>>
}