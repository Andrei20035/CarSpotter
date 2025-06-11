package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserCarApi
import com.example.carspotter.data.remote.model.user.User
import com.example.carspotter.data.remote.model.user_car.UserCar
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCarRepository @Inject constructor(
    private val userCarApi: UserCarApi
) : BaseRepository() {

    suspend fun createUserCar(request: UserCarRequest): ApiResult<Unit> {
        return safeApiCall { userCarApi.createUserCar(request) }
    }

    suspend fun getUserCarById(userCarId: Int): ApiResult<UserCar> {
        return safeApiCall { userCarApi.getUserCarById(userCarId) }
    }

    suspend fun getUserCarByUserId(userId: Int): ApiResult<UserCar> {
        return safeApiCall { userCarApi.getUserCarByUserId(userId) }
    }

    suspend fun getUserByUserCarId(userCarId: Int): ApiResult<User> {
        return safeApiCall { userCarApi.getUserByUserCarId(userCarId) }
    }

    suspend fun updateUserCar(request: UserCarUpdateRequest): ApiResult<Unit> {
        return safeApiCall { userCarApi.updateUserCar(request) }
    }

    suspend fun deleteUserCar(): ApiResult<Unit> {
        return safeApiCall { userCarApi.deleteUserCar() }
    }

    suspend fun getAllUserCars(): ApiResult<List<UserCar>> {
        return safeApiCall { userCarApi.getAllUserCars() }
    }
}
