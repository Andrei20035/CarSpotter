package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.utils.ApiResult

interface IUserCarRepository {
    suspend fun createUserCar(request: UserCarRequest): ApiResult<Unit>
    suspend fun getUserCarById(userCarId: Int): ApiResult<UserCarDTO>
    suspend fun getUserCarByUserId(userId: Int): ApiResult<UserCarDTO>
    suspend fun getUserByUserCarId(userCarId: Int): ApiResult<UserDTO>
    suspend fun updateUserCar(request: UserCarUpdateRequest): ApiResult<Unit>
    suspend fun deleteUserCar(): ApiResult<Unit>
    suspend fun getAllUserCars(): ApiResult<List<UserCarDTO>>
}