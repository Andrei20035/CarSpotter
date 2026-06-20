package com.example.carspotter.features.feed

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.carspotter.R
import com.example.carspotter.core.navigation.Screen
import com.example.carspotter.core.ui.components.FeedNavItem
import com.example.carspotter.core.ui.components.FloatingBottomNav
import com.example.carspotter.core.ui.theme.Poppins
import com.example.carspotter.data.model.FeedPost
import java.util.Locale

// Feed surface color (Figma `feed` background ≈ rgb(5,7,27)).
private val FeedBackground = Color(0xFF05071B)
private val FeedAccent = Color(0xFF34D7C4)
// Discreet dark placeholder shown behind a post image while it loads.
private val ImagePlaceholder = Color(0xFF11162E)
// Figma horizontal margin for cards: 375dp image inside the 402dp frame → ~13dp each side.
private val CardHorizontalPadding = 13.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    var showPostDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    // Prefetch the next page once the last few items become visible.
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val total = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            total > 0 && lastVisible >= total - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadNextPage()
    }

    if (showPostDialog) {
        PostYourFindDialog(onDismiss = { showPostDialog = false })
    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FeedBackground),
    ) {
        // Top scrim — black fading into the navy surface (matches the design's dark top).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarTop + 120.dp)
                .background(Brush.verticalGradient(listOf(Color.Black, Color.Transparent)))
        )

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = CardHorizontalPadding),
                contentPadding = PaddingValues(
                    top = statusBarTop + 16.dp,
                    // Clearance so the floating navbar never covers the last card.
                    bottom = 140.dp,
                ),
            ) {
                when {
                    uiState.isLoadingInitial -> item(key = "initial-loader") {
                        CenteredLoader()
                    }

                    uiState.isEmpty && uiState.errorMessage != null -> item(key = "error") {
                        FeedMessage(
                            title = "Couldn't load the feed",
                            subtitle = uiState.errorMessage,
                            actionLabel = "Retry",
                            onAction = { viewModel.retry() },
                        )
                    }

                    uiState.isEmpty -> item(key = "empty") {
                        FeedMessage(
                            title = "No spots yet",
                            subtitle = "Be the first to share a find.",
                        )
                    }

                    else -> {
                        items(uiState.feedPosts, key = { it.id }) { post ->
                            FeedPostCard(post)
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                        item(key = "footer") {
                            FeedFooter(
                                isLoadingMore = uiState.isLoadingMore,
                                hasMore = uiState.hasMore,
                                loadMoreError = uiState.errorMessage,
                                onRetry = { viewModel.retry() },
                            )
                        }
                    }
                }
            }
        }

        // Bottom scrim behind the floating navbar — surface fading to black.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(146.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        )

        FloatingBottomNav(
            selected = FeedNavItem.Home,
            profilePictureUrl = uiState.currentUser?.profilePicturePath,
            onHome = { /* already on feed */ },
            onLeaderboard = { /* TODO: navigate to leaderboard once the screen exists */ },
            onPlus = { showPostDialog = true },
            onActivity = { /* TODO: navigate to activity once the screen exists */ },
            onProfile = { navController.navigate(Screen.Profile.route) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
        )
    }
}

@Composable
private fun FeedPostCard(post: FeedPost) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // ---- Header: avatar · username · car (+ location) · more ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            AuthorAvatar(url = post.authorProfilePictureUrl)
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = post.username,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.3.sp,
                )
                Spacer(modifier = Modifier.height(3.dp))
                val location = post.locationLabel
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_car),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        // Design: "Porsche 911," — trailing comma when a location follows.
                        text = if (location != null) "${post.carName}," else post.carName,
                        color = Color.White,
                        fontSize = 13.3.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    // Location resolved from the post's coordinates (town, country). Hidden when
                    // the backend hasn't geocoded the post yet — never fabricated.
                    if (location != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(R.drawable.ic_gps),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        LocationText(location, modifier = Modifier.weight(1f, fill = false))
                    }
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_three_dots),
                contentDescription = "More",
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.height(9.dp))

        // ---- Main image (375×468 ≈ aspect 0.80, 18dp radius, soft shadow) ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(375f / 468f)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(18.dp), clip = false)
                .clip(RoundedCornerShape(18.dp))
                .background(ImagePlaceholder),
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = post.carName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Engagement row (indented ~12dp from the image edge) ----
        Row(
            modifier = Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // TODO(like): wire like toggle to LikeRepository; for now reflects server state only.
            Image(
                painter = painterResource(R.drawable.ic_like),
                contentDescription = "Like",
                modifier = Modifier
                    .size(25.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { },
                    ),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formatCount(post.likeCount),
                color = Color.White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = "Comment",
                modifier = Modifier
                    .size(26.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { },
                    ),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formatCount(post.commentCount),
                color = Color.White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
        }

        // ---- Caption ----
        if (!post.caption.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.caption,
                color = Color.White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}

/** Post author's avatar — the real profile picture, falling back to the placeholder. */
@Composable
private fun AuthorAvatar(url: String?) {
    val modifier = Modifier
        .size(37.dp)
        .clip(CircleShape)
    if (url.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            placeholder = painterResource(R.drawable.profile_picture),
            fallback = painterResource(R.drawable.profile_picture),
            error = painterResource(R.drawable.profile_picture),
        )
    }
}

/** Location label with the design's white→transparent right-fade. */
@Composable
private fun LocationText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 13.3.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(
            brush = Brush.horizontalGradient(
                0.0f to Color.White,
                0.85f to Color.White,
                1.0f to Color.White.copy(alpha = 0f),
            ),
        ),
    )
}

@Composable
private fun CenteredLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = FeedAccent)
    }
}

@Composable
private fun FeedMessage(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(subtitle, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onAction) {
                Text(actionLabel, color = FeedAccent, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun FeedFooter(
    isLoadingMore: Boolean,
    hasMore: Boolean,
    loadMoreError: String?,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoadingMore -> CircularProgressIndicator(
                color = FeedAccent,
                modifier = Modifier.size(28.dp),
            )

            loadMoreError != null -> Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Couldn't load more", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                TextButton(onClick = onRetry) {
                    Text("Retry", color = FeedAccent, fontWeight = FontWeight.SemiBold)
                }
            }

            !hasMore -> Text(
                "You're all caught up",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun PostYourFindDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post your find") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Image(
                    painter = painterResource(R.drawable.post_with_camera),
                    contentDescription = "Post with camera",
                    modifier = Modifier.size(120.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Image(
                    painter = painterResource(R.drawable.post_from_gallery),
                    contentDescription = "Post from gallery",
                    modifier = Modifier.size(120.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
    )
}

/** Compact engagement count: 1200 → "1.2K", 1_000_000 → "1M", 341 → "341". */
private fun formatCount(value: Long): String = when {
    value >= 1_000_000 -> trimZero(value / 1_000_000.0) + "M"
    value >= 1_000 -> trimZero(value / 1_000.0) + "K"
    else -> value.toString()
}

private fun trimZero(v: Double): String {
    val s = String.format(Locale.US, "%.1f", v)
    return if (s.endsWith(".0")) s.dropLast(2) else s
}
