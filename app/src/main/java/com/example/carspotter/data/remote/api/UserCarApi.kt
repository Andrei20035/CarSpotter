package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.utils.ApiResult
import retrofit2.http.*

interface UserCarApi {

    @POST("user-cars")
    suspend fun createUserCar(
        @Body userCarRequest: UserCarRequest
    ): ApiResult<Unit>

    @GET("user-cars/{userCarId}")
    suspend fun getUserCarById(
        @Path("userCarId") userCarId: Int
    ): ApiResult<UserCarDTO>

    @GET("user-cars/by-user/{userId}")
    suspend fun getUserCarByUserId(
        @Path("userId") userId: Int
    ): ApiResult<UserCarDTO>

    @GET("user-cars/{userCarId}/user")
    suspend fun getUserByUserCarId(
        @Path("userCarId") userCarId: Int
    ): ApiResult<UserDTO>

    @PUT("user-cars")
    suspend fun updateUserCar(
        @Body userCarUpdateRequest: UserCarUpdateRequest
    ): ApiResult<Unit>

    @DELETE("user-cars")
    suspend fun deleteUserCar(): ApiResult<Unit>

    @GET("user-cars")
    suspend fun getAllUserCars(): ApiResult<List<UserCarDTO>>
}