package com.example.carspotter.data.remote.repository

import com.example.carspotter.data.remote.api.CarModelApi
import com.example.carspotter.data.remote.model.car_model.CarModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarModelRepository @Inject constructor(
    private val carModelApi: CarModelApi
) : BaseRepository() {

    suspend fun getAllCarModels(): ApiResult<List<CarModel>> {
        return safeApiCall { carModelApi.getAllCarModels() }
    }

    suspend fun getCarModelById(modelId: Int): ApiResult<CarModel> {
        return safeApiCall { carModelApi.getCarModelById(modelId) }
    }
}
