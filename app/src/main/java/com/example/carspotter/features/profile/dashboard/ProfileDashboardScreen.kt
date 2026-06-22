package com.example.carspotter.features.profile.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.carspotter.R
import com.example.carspotter.core.navigation.Screen
import com.example.carspotter.core.ui.components.FeedNavItem
import com.example.carspotter.core.ui.components.FloatingBottomNav
import com.example.carspotter.features.feed.components.CommentsSheet
import com.example.carspotter.features.feed.components.rememberPostCreationLauncher

private val DarkBackground = Color(0xFF05081D)
private val CardSurface = Color(0xFF131929)
private val TextMuted = Color(0xFF8A8FA8)

@Composable
fun ProfileDashboardScreen(
    navController: NavController,
    viewModel: ProfileDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()
    val openPostCreation = rememberPostCreationLauncher(navController)

    // Refresh the grid after a post is created from this screen.
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle
            ?.getStateFlow("post_created", false)
            ?.collect { created ->
                if (created) {
                    navController.currentBackStackEntry?.savedStateHandle?.set("post_created", false)
                    viewModel.refresh()
                }
            }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val info = gridState.layoutInfo
            val total = info.totalItemsCount
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            total > 0 && lastVisible >= total - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadNextPage()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 120.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            // ── Header: profile row ──────────────────────────────────────
            item(span = { GridItemSpan(maxLineSpan) }) {
                ProfileHeaderSection(uiState = uiState)
            }

            // ── Header: stats card ───────────────────────────────────────
            item(span = { GridItemSpan(maxLineSpan) }) {
                StatsCard(uiState = uiState)
            }

            // ── Loading / empty / error states ───────────────────────────
            when {
                uiState.isLoadingInitial -> item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                uiState.isEmpty && !uiState.isLoadingUser -> item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("No spots yet", color = TextMuted, fontSize = 15.sp)
                    }
                }

                else -> {
                    items(uiState.posts, key = { it.id }) { post ->
                        AsyncImage(
                            model = post.imageUrl,
                            contentDescription = "${post.brand} ${post.model}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { viewModel.onPostClick(post.id) },
                                ),
                            placeholder = painterResource(R.drawable.profile_picture),
                            error = painterResource(R.drawable.profile_picture),
                        )
                    }

                    if (uiState.isLoadingMore || uiState.hasMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (uiState.isLoadingMore) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom scrim behind the nav bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(146.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        )

        FloatingBottomNav(
            selected = FeedNavItem.Profile,
            profilePictureUrl = uiState.user?.profilePicturePath,
            onHome = {
                navController.navigate(Screen.Feed.route) {
                    popUpTo(Screen.Feed.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onLeaderboard = {
                navController.navigate(Screen.Leaderboard.route) {
                    popUpTo(Screen.Feed.route)
                    launchSingleTop = true
                }
            },
            onPlus = openPostCreation,
            onActivity = { /* TODO */ },
            onProfile = { /* already here */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
        )

        // See-post overlay — shown when a grid image is tapped.
        uiState.selectedPost?.let { post ->
            SeePostOverlay(
                post = post,
                isLikeInFlight = post.id in uiState.likeInFlight,
                onLikeToggle = { viewModel.onLikeToggle(post.id) },
                onOpenComments = { viewModel.openComments(post.id) },
                onDismiss = { viewModel.clearSelectedPost() },
            )
        }

        // Comments sheet — opened from the see-post overlay.
        uiState.commentsSheet?.let { sheet ->
            CommentsSheet(
                state = sheet,
                onDismiss = { viewModel.closeComments() },
                onDraftChange = { viewModel.onCommentDraftChange(it) },
                onSend = { viewModel.submitComment() },
                onRetry = { viewModel.retryLoadComments() },
            )
        }
    }
}

@Composable
private fun ProfileHeaderSection(uiState: ProfileDashboardUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar
        val avatarUrl = uiState.user?.profilePicturePath
        if (avatarUrl.isNullOrBlank()) {
            Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
            )
        } else {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.profile_picture),
                fallback = painterResource(R.drawable.profile_picture),
                error = painterResource(R.drawable.profile_picture),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Username + location + badge
        Column {
            Text(
                text = uiState.user?.username ?: "",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )

            val country = uiState.user?.country?.takeIf { it.isNotBlank() }
            if (country != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_gps),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = country,
                        color = TextMuted,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            EarlySpotterBadge()
        }
    }
}

@Composable
private fun EarlySpotterBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF1A1E2E))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.early_spotter_badge),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "#1 Early Spotter",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun StatsCard(uiState: ProfileDashboardUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurface)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StatItem(label = "SpotScore", value = "${uiState.user?.spotScore ?: 0}")
        StatDivider()
        StreakItem(days = uiState.streakDays)
        StatDivider()
        StatItem(label = "Spots", value = "${uiState.postCount}")
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
private fun StreakItem(days: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Streak", color = TextMuted, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.fire),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "$days Days",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(Color.White.copy(alpha = 0.08f))
    )
}
