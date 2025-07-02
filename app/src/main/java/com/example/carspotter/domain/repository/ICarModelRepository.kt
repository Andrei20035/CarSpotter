package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.car_model.CarModelDTO
import com.example.carspotter.utils.ApiResult

interface ICarModelRepository {
    suspend fun getAllCarModels(): ApiResult<List<CarModelDTO>>
    suspend fun getCarModelById(modelId: Int): ApiResult<CarModelDTO>
    suspend fun getCarModelId(brand: String, model: String): ApiResult<Int>
    suspend fun getAllCarBrands(): ApiResult<List<String>>
    suspend fun getModelsForBrand(brand: String): ApiResult<List<String>>
}
