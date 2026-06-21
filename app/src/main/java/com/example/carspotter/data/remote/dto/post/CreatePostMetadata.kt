package com.example.carspotter.data.remote.dto.post

import com.example.carspotter.core.network.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * JSON `metadata` part of the multipart `POST /posts` request. Mirrors the server
 * `CreatePostMetadata`; all fields are optional so the client only sends what it has
 * (this screen sends [carModelId] and [caption]).
 */
@Serializable
data class CreatePostMetadata(
    @Serializable(with = UUIDSerializer::class)
    val carModelId: UUID? = null,
    val customBrand: String? = null,
    val customModel: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val town: String? = null,
    val country: String? = null,
    val caption: String? = null,
)
