package com.example.carspotter.data.repository

import com.example.carspotter.data.remote.api.UploadImageApi
import com.example.carspotter.data.remote.model.user.UploadImageRequest
import com.example.carspotter.data.remote.model.user.UploadUrlResponse
import com.example.carspotter.domain.repository.IImageRepository
import com.example.carspotter.utils.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val uploadImageApi: UploadImageApi
) : BaseRepository(), IImageRepository {

    override suspend fun getUploadUrl(request: UploadImageRequest): ApiResult<UploadUrlResponse> {
        return safeApiCall { uploadImageApi.getUploadUrl(request) }
    }

    override suspend fun uploadImageAndGetPublicUrl(request: UploadImageRequest, imageBytes: ByteArray, mimeType: String): String {
        val uploadResponse = getUploadUrl(request)
        return when (uploadResponse) {
            is ApiResult.Success -> {
                uploadToS3(uploadResponse.data.uploadUrl, imageBytes, mimeType)
                uploadResponse.data.publicUrl
            }
            is ApiResult.Error -> throw IOException("Failed to get upload URL: ${uploadResponse.message}")
        }
    }

    private suspend fun uploadToS3(
        uploadUrl: String,
        imageBytes: ByteArray,
        mimeType: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(uploadUrl)
                .put(imageBytes.toRequestBody(mimeType.toMediaTypeOrNull()))
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (!it.isSuccessful) {
                    throw IOException("Upload failed with status code ${it.code}")
                }
                true
            }
        }
    }
}
