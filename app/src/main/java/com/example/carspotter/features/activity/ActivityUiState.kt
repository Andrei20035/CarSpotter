package com.example.carspotter.features.activity

import com.example.carspotter.data.model.User
import com.example.carspotter.features.activity.model.ActivityItem

data class ActivityUiState(
    val currentUser: User? = null,
    val weeklySpotScore: Int = 0,
    val todayInteractions: Int = 0,
    val items: List<ActivityItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
) {
    val isEmpty: Boolean get() = items.isEmpty()
}
