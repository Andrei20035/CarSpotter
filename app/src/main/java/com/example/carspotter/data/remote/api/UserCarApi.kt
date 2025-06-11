package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.User
import com.example.carspotter.data.remote.model.user_car.UserCar
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserCarApi {

    @POST("/user-cars")
    suspend fun createUserCar(
        @Body userCarRequest: UserCarRequest
    ): Response<Unit>

    @GET("/user-cars/{userCarId}")
    suspend fun getUserCarById(
        @Path("userCarId") userCarId: Int
    ): Response<UserCar>

    @GET("/user-cars/by-user/{userId}")
    suspend fun getUserCarByUserId(
        @Path("userId") userId: Int
    ): Response<UserCar>

    @GET("/user-cars/{userCarId}/user")
    suspend fun getUserByUserCarId(
        @Path("userCarId") userCarId: Int
    ): Response<User>

    @PUT("/user-cars")
    suspend fun updateUserCar(
        @Body userCarUpdateRequest: UserCarUpdateRequest
    ): Response<Unit>

    @DELETE("/user-cars")
    suspend fun deleteUserCar(): Response<Unit>

    @GET("/user-cars")
    suspend fun getAllUserCars(): Response<List<UserCar>>
}