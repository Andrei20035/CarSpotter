package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.car_model.CarModelDTO
import com.example.carspotter.data.remote.model.car_model.CarModelIdResponse
import com.example.carspotter.domain.model.CarModel
import com.example.carspotter.utils.ApiResult
import java.util.UUID

interface ICarModelRepository {
    suspend fun getAllCarModels(): ApiResult<List<CarModel>>
    suspend fun getCarModelById(modelId: UUID): ApiResult<CarModel>
    suspend fun getCarModelId(brand: String, model: String): ApiResult<CarModelIdResponse>
    suspend fun getAllCarBrands(): ApiResult<List<String>>
    suspend fun getModelsForBrand(brand: String): ApiResult<List<String>>
}
