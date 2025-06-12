package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.CarModelApi
import com.example.carspotter.data.remote.model.car_model.CarModelDTO
import com.example.carspotter.domain.repository.ICarModelRepository
import com.example.carspotter.utils.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarModelRepository @Inject constructor(
    private val carModelApi: CarModelApi
) : BaseRepository(), ICarModelRepository {

    override suspend fun getAllCarModels(): ApiResult<List<CarModelDTO>> {
        return safeApiCall { carModelApi.getAllCarModels() }
    }

    override suspend fun getCarModelById(modelId: Int): ApiResult<CarModelDTO> {
        return safeApiCall { carModelApi.getCarModelById(modelId) }
    }
}
