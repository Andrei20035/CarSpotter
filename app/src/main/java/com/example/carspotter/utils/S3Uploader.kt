package com.example.carspotter.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

suspend fun uploadToS3(
    uploadUrl: String,
    imageBytes: ByteArray
): Boolean {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(uploadUrl)
            .put(imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull()))
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