package com.example.carspotter.features.feed

import com.example.carspotter.data.model.FeedPost
import com.example.carspotter.data.model.User
import com.example.carspotter.data.remote.dto.post.FeedCursor

data class FeedUiState(
    val currentUser: User? = null,

    val feedPosts: List<FeedPost> = emptyList(),
    val nextCursor: FeedCursor? = null,
    val hasMore: Boolean = true,

    // First page into an empty list — show a full-screen loader.
    val isLoadingInitial: Boolean = false,
    // Pull-to-refresh while content may already be on screen.
    val isRefreshing: Boolean = false,
    // Appending the next page — show a footer loader.
    val isLoadingMore: Boolean = false,

    val errorMessage: String? = null,
) {
    val isEmpty: Boolean
        get() = feedPosts.isEmpty()

    val isAnyLoading: Boolean
        get() = isLoadingInitial || isRefreshing || isLoadingMore
}
