package com.example.carspotter.features.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.data.repository.PostRepository
import com.example.carspotter.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        loadFirstPage()
    }

    /** Initial load into an empty feed. */
    private fun loadFirstPage() = load(reset = true, isRefresh = false)

    /** Pull-to-refresh: reload from the top, keeping current content visible until it returns. */
    fun refresh() = load(reset = true, isRefresh = true)

    /** Infinite scroll: append the next page if there is one and nothing is already in flight. */
    fun loadNextPage() {
        val state = _uiState.value
        if (!state.hasMore || state.isAnyLoading) return
        load(reset = false, isRefresh = false)
    }

    /** Retry after an error — reloads the first page when empty, otherwise retries the next page. */
    fun retry() {
        val state = _uiState.value
        if (state.isAnyLoading) return
        load(reset = state.isEmpty, isRefresh = false)
    }

    private fun load(reset: Boolean, isRefresh: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingInitial = reset && !isRefresh && it.isEmpty,
                    isRefreshing = isRefresh,
                    isLoadingMore = !reset,
                    errorMessage = null,
                )
            }

            val cursor = if (reset) null else _uiState.value.nextCursor

            when (val result = postRepository.getFeedPosts(limit = PAGE_SIZE, cursor = cursor)) {
                is ApiResult.Success -> _uiState.update { state ->
                    val incoming = result.data.posts
                    val merged = if (reset) {
                        incoming
                    } else {
                        // Append, de-duplicating on id in case a new post shifted the page window.
                        (state.feedPosts + incoming).distinctBy { it.id }
                    }
                    state.copy(
                        feedPosts = merged,
                        nextCursor = result.data.nextCursor,
                        hasMore = result.data.hasMore,
                        isLoadingInitial = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                    )
                }

                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        isLoadingInitial = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMessage = result.message,
                    )
                }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is ApiResult.Success -> _uiState.update { it.copy(currentUser = result.data) }
                is ApiResult.Error -> Unit // header avatar falls back to placeholder; not fatal to the feed
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 15
    }
}
