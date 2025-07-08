package com.example.carspotter.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carspotter.data.repository.PostRepository
import com.example.carspotter.ui.screens.login.LoginUiState
import com.example.carspotter.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadCurrentDayPosts()
        loadFeedPosts()
    }

    private fun loadCurrentDayPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCurrentDay = true) }

            when(val result = postRepository.getCurrentDayPostsForUser()) {
                is ApiResult.Success -> _uiState.update { it.copy(currentDayPosts = result.data) }
                is ApiResult.Error -> setError(result.message)
            }
            _uiState.update { it.copy(isLoadingCurrentDay = false) }
        }
    }


    private fun loadFeedPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingFeed = true) }

//            when(val result = postRepository.getFeedPosts()) {
//                is ApiResult.Success -> _uiState.update { it.copy(feedPosts = result.data) }
//                is ApiResult.Error -> setError(result.message)
//            }
            _uiState.update { it.copy(isLoadingFeed = false) }
        }
    }



    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }

        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(errorMessage = null) }
        }
    }


}