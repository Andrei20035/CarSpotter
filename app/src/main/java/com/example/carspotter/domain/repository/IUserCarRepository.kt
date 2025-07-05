package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.domain.model.User
import com.example.carspotter.domain.model.UserCar
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface IUserCarRepository {
    suspend fun createUserCar(request: UserCarRequest): ApiResult<Unit>
    suspend fun getUserCarById(userCarId: UUID): ApiResult<UserCar>
    suspend fun getUserCarByUserId(userId: UUID): ApiResult<UserCar>
    suspend fun getUserByUserCarId(userCarId: UUID): ApiResult<User>
    suspend fun updateUserCar(request: UserCarUpdateRequest): ApiResult<Unit>
    suspend fun deleteUserCar(): ApiResult<Unit>
    suspend fun getAllUserCars(): ApiResult<List<UserCar>>
}