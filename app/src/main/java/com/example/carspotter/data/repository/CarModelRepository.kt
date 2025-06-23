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
        return carModelApi.getAllCarModels()
    }

    override suspend fun getCarModelById(modelId: Int): ApiResult<CarModelDTO> {
        return carModelApi.getCarModelById(modelId)
    }

    override suspend fun getCarModelId(
        brand: String,
        model: String
    ): ApiResult<Int> {
        return carModelApi.getCarModelId(brand, model)
    }

    override suspend fun getAllCarBrands(): ApiResult<List<String>> {
        return carModelApi.getAllCarBrands()
    }

    override suspend fun getCarModelsForBrand(brand: String): ApiResult<List<String>> {
        return carModelApi.getCarModelsForBrand(brand)
    }
}
