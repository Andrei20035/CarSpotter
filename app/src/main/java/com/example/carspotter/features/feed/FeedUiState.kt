package com.example.carspotter.features.feed

import com.example.carspotter.data.model.Post
import com.example.carspotter.data.model.User

data class FeedUiState(
    val currentDayPosts: List<Post> = emptyList(),
    val feedPosts: List<Post> = emptyList(),
    val currentUser: User? = null,

    val isLoadingCurrentDay: Boolean = false,
    val isLoadingFeed: Boolean = false,
    val isLoadingCurrentUser: Boolean = false,

    val errorMessage: String? = null
)
