package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.dto.user.UploadImageRequest
import com.example.carspotter.data.remote.dto.user.UploadUrlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UploadImageApi {
    @POST("upload-url")
    suspend fun getUploadUrl(
        @Body uploadImageRequest: UploadImageRequest
    ): Response<UploadUrlResponse>
}