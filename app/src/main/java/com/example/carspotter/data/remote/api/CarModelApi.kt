package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.car_model.CarModelDTO
import com.example.carspotter.utils.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CarModelApi {
    @GET("car-models")
    suspend fun getAllCarModels(): ApiResult<List<CarModelDTO>>

    @GET("car-models/{modelId}")
    suspend fun getCarModelById(
        @Path("modelId") modelId: Int
    ): ApiResult<CarModelDTO>

    @GET("car-models/id")
    suspend fun getCarModelId(
        @Query("brand") brand: String,
        @Query("model") model: String
    ): ApiResult<Int>

    @GET("car-models/brands")
    suspend fun getAllCarBrands(): ApiResult<List<String>>

    @GET("car-models/brands/{brand}/models")
    suspend fun getModelsForBrand(
        @Path("brand") brand: String
    ): ApiResult<List<String>>
}