package com.example.carspotter.features.leaderboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.carspotter.core.navigation.Screen
import com.example.carspotter.core.ui.components.AppScreenBackground
import com.example.carspotter.core.ui.components.FeedNavItem
import com.example.carspotter.core.ui.components.FloatingBottomNav
import com.example.carspotter.features.feed.components.rememberPostCreationLauncher
import com.example.carspotter.features.leaderboard.components.CurrentUserLeaderboardCard
import com.example.carspotter.features.leaderboard.components.LeaderboardUserRow
import com.example.carspotter.features.leaderboard.components.PodiumSection

@Composable
fun LeaderboardScreen(
    navController: NavHostController,
    viewModel: LeaderboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val openPostCreation = rememberPostCreationLauncher(navController)

    AppScreenBackground(
        foreground = {
            FloatingBottomNav(
                selected = FeedNavItem.Leaderboard,
                profilePictureUrl = uiState.currentUser?.entry?.avatarUrl,
                onHome = {
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Feed.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLeaderboard = { /* already here */ },
                onPlus = openPostCreation,
                onActivity = { /* TODO */ },
                onProfile = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Feed.route)
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp),
            )
        },
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            uiState.errorMessage != null -> {
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 140.dp),
                ) {
                    // ── Current-user summary card ─────────────────────────
                    uiState.currentUser?.let { standing ->
                        item {
                            CurrentUserLeaderboardCard(
                                standing = standing,
                                modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp),
                            )
                        }
                    }

                    // ── Podium (ranks 1–3) ────────────────────────────────
                    if (uiState.podium.size >= 3) {
                        item {
                            PodiumSection(podium = uiState.podium)
                        }
                    }

                    // ── Rank 4+ rows ──────────────────────────────────────
                    items(uiState.rest, key = { it.userId }) { entry ->
                        LeaderboardUserRow(
                            entry = entry,
                            modifier = Modifier.padding(
                                horizontal = 13.dp,
                                vertical = 4.dp,
                            ),
                        )
                    }
                }
            }
        }
    }
}
