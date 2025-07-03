package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.model.user.UploadImageRequest
import com.example.carspotter.data.remote.model.user.UploadUrlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadImageApi {
    @POST("upload-url")
    suspend fun getUploadUrl(
        @Body uploadImageRequest: UploadImageRequest
    ): Response<UploadUrlResponse>
}