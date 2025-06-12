package com.example.carspotter.ui.screens.profile_setup

import androidx.lifecycle.ViewModel
import com.example.carspotter.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

}