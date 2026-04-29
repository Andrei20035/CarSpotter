package com.example.carspotter.data.remote.api

import com.example.carspotter.data.model.CarModel
import com.example.carspotter.data.remote.dto.car_model.CarModelIdResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface CarModelApi {
    @GET("car-models")
    suspend fun getAllCarModels(): Response<List<CarModel>>

    @GET("car-models/{modelId}")
    suspend fun getCarModelById(
        @Path("modelId") modelId: UUID
    ): Response<CarModel>

    @GET("car-models/id")
    suspend fun getCarModelId(
        @Query("brand") brand: String,
        @Query("model") model: String
    ): Response<CarModelIdResponse>

    @GET("car-models/brands")
    suspend fun getAllCarBrands(): Response<List<String>>

    @GET("car-models/brands/{brand}/models")
    suspend fun getModelsForBrand(
        @Path("brand") brand: String
    ): Response<List<String>>
}