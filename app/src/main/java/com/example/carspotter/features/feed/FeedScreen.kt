package com.example.carspotter.features.feed

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.carspotter.core.ui.components.GradientText
import com.example.carspotter.data.model.FeedPost

// Background color shared with the rest of the feed surface.
private val FeedBackground = Color(0xFF05081D)

// TODO(current-day): The server has no `/posts/current-day` endpoint yet, so the
// "today" carousel below is still hardcoded mock data. It is kept to preserve the
// screen layout; replace with real data once the endpoint exists (or hide it).
private data class CurrentDayPost(
    @DrawableRes val imageRes: Int,
    val points: Int,
    val dateTime: String,
    val carName: String,
    val location: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val currentDayPosts = remember {
        listOf(
            CurrentDayPost(R.drawable.feed_image1, 220, "8 Feb 16:42", "Ferrari 488 Pista", "London, UK"),
            CurrentDayPost(R.drawable.feed_image2, 190, "8 Feb 18:11", "Lamborghini Aventador", "London, UK"),
            CurrentDayPost(R.drawable.feed_image3, 260, "9 Feb 09:14", "Porsche 911 GT3", "London, UK"),
            CurrentDayPost(R.drawable.feed_image4, 310, "9 Feb 12:55", "McLaren 720S", "London, UK"),
        )
    }

    var showPostDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    // Trigger pagination when the last few items become visible.
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

    Scaffold(
        topBar = { FeedTopBar(navController, uiState.currentUser?.profilePicturePath) },
        containerColor = FeedBackground,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            // Soft gradient behind the top of the content.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(Brush.verticalGradient(listOf(Color.Black, FeedBackground)))
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
                        .padding(horizontal = 16.dp),
                    // Bottom padding so the floating navbar never covers the last card.
                    contentPadding = PaddingValues(top = 58.dp, bottom = 128.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item(key = "header") {
                        FeedHeader(
                            currentDayPosts = currentDayPosts,
                            onPostYourFind = { showPostDialog = true },
                        )
                    }

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
                                Spacer(modifier = Modifier.height(22.dp))
                                FeedPostCard(post)
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

            FloatingBottomNav(
                selected = FeedNavItem.Home,
                onHome = { /* already on feed */ },
                onLeaderboard = { /* TODO: navigate to leaderboard once the screen exists */ },
                onPlus = { showPostDialog = true },
                onActivity = { /* TODO: navigate to activity once the screen exists */ },
                onProfile = { navController.navigate(Screen.Profile.route) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
            )
        }
    }
}

@Composable
private fun FeedTopBar(navController: NavController, profilePicturePath: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Black.copy(alpha = 0.78f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Transparent
                    )
                )
            )
            .padding(top = 16.dp, start = 8.dp, end = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = "Friends",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            GradientText(
                text = "Revio",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            val profileModifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .clickable { navController.navigate(Screen.Profile.route) }
            if (profilePicturePath.isNullOrBlank()) {
                Image(
                    painter = painterResource(R.drawable.profile_picture),
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = profileModifier
                )
            } else {
                AsyncImage(
                    model = profilePicturePath,
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = profileModifier,
                    fallback = painterResource(R.drawable.profile_picture),
                    error = painterResource(R.drawable.profile_picture)
                )
            }
        }
    }
}

@Composable
private fun FeedHeader(
    currentDayPosts: List<CurrentDayPost>,
    onPostYourFind: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { currentDayPosts.size })
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(42.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) { page ->
            CurrentDayCard(currentDayPosts[page])
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(currentDayPosts.size) { index ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (pagerState.currentPage == index) Color.White else Color(0xFF696969))
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        PostYourFindButton(onClick = onPostYourFind)
    }
}

@Composable
private fun CenteredLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Color(0xFF34D7C4))
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
            .padding(vertical = 56.dp),
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
                Text(actionLabel, color = Color(0xFF34D7C4), fontWeight = FontWeight.SemiBold)
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
                color = Color(0xFF34D7C4),
                modifier = Modifier.size(28.dp),
            )

            loadMoreError != null -> Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Couldn't load more", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                TextButton(onClick = onRetry) {
                    Text("Retry", color = Color(0xFF34D7C4), fontWeight = FontWeight.SemiBold)
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
private fun CurrentDayCard(post: CurrentDayPost) {
    Row(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(post.imageRes),
            contentDescription = post.carName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .fillMaxSize()
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PostMetaRow(R.drawable.ic_points, "${post.points} points")
            PostMetaRow(R.drawable.ic_clock, post.dateTime)
            PostMetaRow(R.drawable.ic_car, post.carName)
            PostMetaRow(R.drawable.ic_gps, post.location)
        }
    }
}

@Composable
private fun PostMetaRow(@DrawableRes iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PostYourFindButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFD96570), Color(0xFFA470BE))
                )
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 42.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Post your find",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
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
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(R.drawable.post_from_gallery),
                    contentDescription = "Post from gallery",
                    modifier = Modifier.size(120.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun FeedPostCard(post: FeedPost) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // TODO(avatar): feed DTO has no author avatar yet — placeholder for now.
            Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(post.username, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_car),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(post.carName, color = Color.White, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_three_dots),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.85f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF11162E)) // discreet dark placeholder behind the image
        ) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = post.carName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.20f))
                    .padding(vertical = 6.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO(like): wire like toggle to LikeRepository; for now reflects server state only.
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(R.drawable.ic_like),
                        contentDescription = "Like",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(post.likeCount.toString(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(R.drawable.ic_comment),
                        contentDescription = "Comment",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(post.commentCount.toString(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
            }
        }

        if (!post.caption.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.caption,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
