package com.example.carspotter.features.feed

import com.example.carspotter.data.model.Post

data class FeedUiState(
    val currentDayPosts: List<Post> = emptyList(),
    val feedPosts: List<Post> = emptyList(),

    val isLoadingCurrentDay: Boolean = false,
    val isLoadingFeed: Boolean = false,

    val errorMessage: String? = null
)