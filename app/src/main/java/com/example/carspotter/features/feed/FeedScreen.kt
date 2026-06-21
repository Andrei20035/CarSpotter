package com.example.carspotter.features.feed

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.carspotter.R
import java.io.File
import com.example.carspotter.core.navigation.Screen
import com.example.carspotter.core.ui.components.CustomSnackbar
import com.example.carspotter.core.ui.components.FeedNavItem
import com.example.carspotter.core.ui.components.FloatingBottomNav
import com.example.carspotter.core.ui.theme.Poppins
import com.example.carspotter.data.model.FeedPost
import com.example.carspotter.data.model.ReportReason
import com.example.carspotter.features.feed.components.CarLocationRow
import com.example.carspotter.features.feed.components.CommentsSheet
import com.example.carspotter.features.feed.components.FeedPostSkeleton
import com.example.carspotter.features.feed.components.PostOptionsMenu
import com.example.carspotter.features.feed.components.PostYourFindOverlay
import com.example.carspotter.features.feed.components.SubmitReportDialog
import kotlinx.coroutines.delay
import java.util.Locale

// Feed surface color (Figma `feed` background ≈ rgb(5,7,27)).
private val FeedBackground = Color(0xFF05071B)
private val FeedAccent = Color(0xFF34D7C4)
// Discreet dark placeholder shown behind a post image while it loads.
private val ImagePlaceholder = Color(0xFF11162E)
// Figma horizontal margin for cards: 375dp image inside the 402dp frame → ~13dp each side.
private val CardHorizontalPadding = 13.dp
// Number of shimmer skeleton cards shown during the initial feed load.
private const val SKELETON_COUNT = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showPostDialog by remember { mutableStateOf(false) }

    // Capture (camera) or pick (gallery) a photo, then hand it to the image-upload screen.
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        if (uri != null) {
            navController.navigate(Screen.ImageUpload.createRoute(uri.toString()))
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        val uri = pendingCameraUri
        if (success && uri != null) {
            navController.navigate(Screen.ImageUpload.createRoute(uri.toString()))
        }
    }

    // "Submit Report" confirmation — driven entirely by the ViewModel (UDF).
    uiState.reportDialog?.let { dialog ->
        SubmitReportDialog(
            reason = dialog.reason,
            isSubmitting = dialog.isSubmitting,
            onConfirm = { viewModel.confirmReport() },
            onDismiss = { viewModel.dismissReportDialog() },
        )
    }

    // Instagram-style comments overlay — opens over the feed, driven by the ViewModel (UDF).
    uiState.commentsSheet?.let { sheet ->
        CommentsSheet(
            state = sheet,
            onDismiss = { viewModel.closeComments() },
            onDraftChange = { viewModel.onCommentDraftChange(it) },
            onSend = { viewModel.submitComment() },
            onRetry = { viewModel.retryLoadComments() },
        )
    }

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
        PostYourFindOverlay(
            onCamera = {
                showPostDialog = false
                val uri = createCameraImageUri(context)
                pendingCameraUri = uri
                cameraLauncher.launch(uri)
            },
            onGallery = {
                showPostDialog = false
                galleryLauncher.launch("image/*")
            },
            onDismiss = { showPostDialog = false },
        )
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
                    top = 16.dp,
                    // Clearance so the floating navbar never covers the last card.
                    bottom = 140.dp,
                ),
            ) {
                when {
                    uiState.isLoadingInitial -> items(SKELETON_COUNT, key = { "skeleton-$it" }) {
                        FeedPostSkeleton()
                        Spacer(modifier = Modifier.height(30.dp))
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
                            FeedPostCard(
                                post = post,
                                onLikeToggle = { viewModel.onLikeToggle(post.id) },
                                onOpenComments = { viewModel.openComments(post.id) },
                                onShare = { sharePost(context, post) },
                                onReportReasonSelected = { reason ->
                                    viewModel.onReportReasonSelected(post.id, reason)
                                },
                            )
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

        // One-shot feedback (e.g. report submitted) — auto-dismisses after a short delay.
        uiState.userMessage?.let { message ->
            LaunchedEffect(message) {
                delay(3000)
                viewModel.consumeUserMessage()
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 96.dp),
            ) {
                CustomSnackbar(message = message)
            }
        }
    }
}

@Composable
private fun FeedPostCard(
    post: FeedPost,
    onLikeToggle: () -> Unit,
    onOpenComments: () -> Unit,
    onShare: () -> Unit,
    onReportReasonSelected: (ReportReason) -> Unit,
) {
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
                CarLocationRow(
                    carName = post.carName,
                    location = post.locationLabel,
                )
            }
            PostOptionsMenu(
                onShare = onShare,
                onReportReasonSelected = onReportReasonSelected,
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
        // Each count sits in a small adaptive-width slot so the comment group doesn't shift on
        // small count changes (0↔1, 1↔9), while still staying compact for single digits.
        Row(
            modifier = Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InteractionItem(count = post.likeCount, onClick = onLikeToggle) {
                // Icon is driven by this post's own server-backed liked state — never hardcoded.
                LikeIcon(liked = post.likedByCurrentUser)
            }
            Spacer(modifier = Modifier.width(12.dp))
            InteractionItem(count = post.commentCount, onClick = onOpenComments) {
                Image(
                    painter = painterResource(R.drawable.comment),
                    contentDescription = "Comments",
                    modifier = Modifier.size(26.dp),
                )
            }
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

/**
 * A single feed interaction (icon + count) laid out as `Row { Icon; CountText }`. The whole row
 * is the tap target. The count lives in a small fixed-width slot sized by [interactionCountWidth]
 * so neighbouring groups don't jump as the count changes between small values.
 */
@Composable
private fun InteractionItem(
    count: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(modifier = Modifier.width(6.dp))
        Box(modifier = Modifier.width(interactionCountWidth(count))) {
            Text(
                text = formatCount(count),
                color = Color.White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 1,
                // Tabular figures keep each digit the same width, so the slot stays stable.
                style = TextStyle(fontFeatureSettings = "tnum"),
            )
        }
    }
}

/**
 * Adaptive width for a count slot, sized to the widest string the count can format to in each
 * magnitude band (matching [formatCount]). Small counts get a compact slot — no large empty gap —
 * while larger counts expand in controlled steps so the layout never visibly jumps on a 0↔1 change.
 */
private fun interactionCountWidth(count: Long): Dp = when {
    count < 10 -> 16.dp       // "0".."9"
    count < 100 -> 24.dp      // "10".."99"
    count < 1_000 -> 32.dp    // "100".."999"
    count < 10_000 -> 40.dp   // "1K".."9.9K"
    count < 100_000 -> 48.dp  // "10K".."99.9K"
    else -> 56.dp             // "100K"+, "1M"+
}

/**
 * Like icon. Shows `like_selected` when liked, `like` otherwise. On a like (false→true) it plays a
 * subtle pop — a quick scale-up that springs back — for premium tactile feedback. Unliking does not
 * animate, and the initial liked state on first composition is not animated. The tap is handled by
 * the enclosing [InteractionItem].
 */
@Composable
private fun LikeIcon(
    liked: Boolean,
    modifier: Modifier = Modifier,
) {
    val scale = remember { Animatable(1f) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(liked) {
        if (!initialized) {
            initialized = true
            return@LaunchedEffect
        }
        if (liked) {
            scale.animateTo(1.22f, animationSpec = tween(durationMillis = 110, easing = FastOutSlowInEasing))
            scale.animateTo(1f, animationSpec = spring(dampingRatio = 0.42f, stiffness = Spring.StiffnessMedium))
        }
    }

    Image(
        painter = painterResource(if (liked) R.drawable.like_selected else R.drawable.like),
        contentDescription = if (liked) "Unlike" else "Like",
        modifier = modifier
            .size(30.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            },
    )
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

/**
 * Creates a FileProvider-backed URI in the cache for the camera app to write a capture into.
 * Mirrors the authority declared in the manifest (`${applicationId}.fileprovider`).
 */
private fun createCameraImageUri(context: Context): Uri {
    val dir = File(context.cacheDir, "camera").apply { mkdirs() }
    val file = File.createTempFile("capture_", ".jpg", dir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

/**
 * Fires the standard Android share sheet (`ACTION_SEND`) for a post — the car label and a link
 * to the photo. This is a client-only action; no backend call is involved.
 */
private fun sharePost(context: Context, post: FeedPost) {
    val shareText = buildString {
        append("Check out this ${post.carName} on CarSpotter")
        if (post.imageUrl.isNotBlank()) {
            append("\n")
            append(post.imageUrl)
        }
    }
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share post"))
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
