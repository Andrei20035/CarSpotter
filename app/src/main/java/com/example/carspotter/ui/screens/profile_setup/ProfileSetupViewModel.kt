package com.example.carspotter.ui.screens.profile_setup

import androidx.lifecycle.ViewModel
import com.example.carspotter.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileCustomizationViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileCustomizationUiState())
    val uiState: StateFlow<ProfileCustomizationUiState> = _uiState.asStateFlow()

    fun updateFullName(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateCountry(country: String) {
        _uiState.update { it.copy(country = country) }
    }

}