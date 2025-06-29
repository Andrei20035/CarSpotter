package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserCarApi
import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.domain.repository.IUserCarRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCarRepository @Inject constructor(
    private val userCarApi: UserCarApi
) : BaseRepository(), IUserCarRepository {

    override suspend fun createUserCar(request: UserCarRequest): ApiResult<Unit> {
        return userCarApi.createUserCar(request)
    }

    override suspend fun getUserCarById(userCarId: Int): ApiResult<UserCarDTO> {
        return userCarApi.getUserCarById(userCarId)
    }

    override suspend fun getUserCarByUserId(userId: Int): ApiResult<UserCarDTO> {
        return userCarApi.getUserCarByUserId(userId)
    }

    override suspend fun getUserByUserCarId(userCarId: Int): ApiResult<UserDTO> {
        return userCarApi.getUserByUserCarId(userCarId)
    }

    override suspend fun updateUserCar(request: UserCarUpdateRequest): ApiResult<Unit> {
        return userCarApi.updateUserCar(request)
    }

    override suspend fun deleteUserCar(): ApiResult<Unit> {
        return userCarApi.deleteUserCar()
    }

    override suspend fun getAllUserCars(): ApiResult<List<UserCarDTO>> {
        return userCarApi.getAllUserCars()
    }
}
