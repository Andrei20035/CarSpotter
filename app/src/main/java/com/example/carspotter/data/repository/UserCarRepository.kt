package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UserCarApi
import com.example.carspotter.data.remote.model.user.UserDTO
import com.example.carspotter.data.remote.model.user_car.UserCarDTO
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.domain.model.User
import com.example.carspotter.domain.model.UserCar
import com.example.carspotter.domain.model.toDomain
import com.example.carspotter.domain.repository.IUserCarRepository
import com.example.carspotter.utils.ApiResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCarRepository @Inject constructor(
    private val userCarApi: UserCarApi
) : BaseRepository(), IUserCarRepository {

    override suspend fun createUserCar(request: UserCarRequest): ApiResult<Unit> {
        return safeApiCall { userCarApi.createUserCar(request)}
    }

    override suspend fun getUserCarById(userCarId: UUID): ApiResult<UserCar> {
        return when (val result = safeApiCall { userCarApi.getUserCarById(userCarId)}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getUserCarByUserId(userId: UUID): ApiResult<UserCar> {
        return when (val result = safeApiCall { userCarApi.getUserCarByUserId(userId)}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getUserByUserCarId(userCarId: UUID): ApiResult<User> {
        return when (val result = safeApiCall { userCarApi.getUserByUserCarId(userCarId)}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun updateUserCar(request: UserCarUpdateRequest): ApiResult<Unit> {
        return safeApiCall { userCarApi.updateUserCar(request)}
    }

    override suspend fun deleteUserCar(): ApiResult<Unit> {
        return safeApiCall { userCarApi.deleteUserCar()}
    }

    override suspend fun getAllUserCars(): ApiResult<List<UserCar>> {
        return when (val result = safeApiCall { userCarApi.getAllUserCars()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
