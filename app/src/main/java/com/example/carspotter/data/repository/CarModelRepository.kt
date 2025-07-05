package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.CarModelApi
import com.example.carspotter.data.remote.model.car_model.CarModelDTO
import com.example.carspotter.data.remote.model.car_model.CarModelIdResponse
import com.example.carspotter.domain.model.CarModel
import com.example.carspotter.domain.model.toDomain
import com.example.carspotter.domain.repository.ICarModelRepository
import com.example.carspotter.utils.ApiResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarModelRepository @Inject constructor(
    private val carModelApi: CarModelApi
) : BaseRepository(), ICarModelRepository {

    override suspend fun getAllCarModels(): ApiResult<List<CarModel>> {
        return when(val result = safeApiCall { carModelApi.getAllCarModels()}) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getCarModelById(modelId: UUID): ApiResult<CarModel> {
        return when(val result = safeApiCall { carModelApi.getCarModelById(modelId) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun getCarModelId(
        brand: String,
        model: String
    ): ApiResult<CarModelIdResponse> {
        return safeApiCall { carModelApi.getCarModelId(brand, model) }
    }

    override suspend fun getAllCarBrands(): ApiResult<List<String>> {
        return safeApiCall { carModelApi.getAllCarBrands() }
    }

    override suspend fun getModelsForBrand(brand: String): ApiResult<List<String>> {
        return safeApiCall { carModelApi.getModelsForBrand(brand) }
    }
}
