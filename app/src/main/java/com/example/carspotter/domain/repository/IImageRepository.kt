package com.example.carspotter.domain.repository

import com.example.carspotter.data.remote.model.user.UploadImageRequest
import com.example.carspotter.data.remote.model.user.UploadUrlResponse
import com.example.carspotter.utils.ApiResult

interface IImageRepository {
    suspend fun getUploadUrl(request: UploadImageRequest): ApiResult<UploadUrlResponse>
    suspend fun uploadImageAndGetPublicUrl(request: UploadImageRequest, imageBytes: ByteArray, mimeType: String): String
}
