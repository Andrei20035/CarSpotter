package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.car_model.CarModel
import retrofit2.Response
import retrofit2.http.GET

interface CarModelApi {

    @GET("/car-models")
    suspend fun getAllCarModels(): Response<List<CarModel>>

    @GET("/car-models/{modelId}")
    suspend fun getCarModelById(
        @retrofit2.http.Path("modelId") modelId: Int
    ): Response<CarModel>
}