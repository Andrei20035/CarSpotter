package com.example.carspotter.features.settings

import com.example.carspotter.data.model.User

data class SettingsUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
)
