package com.example.carspotter.features.profile.dashboard

import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import coil3.compose.AsyncImage
import com.example.carspotter.R
import com.example.carspotter.core.util.toPostDate
import com.example.carspotter.data.model.FeedPost
import java.util.Locale

@Composable
fun SeePostOverlay(
    post: FeedPost,
    isLikeInFlight: Boolean,
    onLikeToggle: () -> Unit,
    onOpenComments: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        val context = LocalContext.current
        val blurRadiusPx = with(LocalDensity.current) { 28.dp.roundToPx() }
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

        LaunchedEffect(dialogWindow) {
            val window = dialogWindow ?: return@LaunchedEffect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val blurEnabled = context.getSystemService(WindowManager::class.java)
                    ?.isCrossWindowBlurEnabled == true
                window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                window.attributes = window.attributes.apply { blurBehindRadius = blurRadiusPx }
                window.setDimAmount(if (blurEnabled) 0.25f else 0.5f)
            } else {
                window.setDimAmount(0.5f)
            }
        }

        // Outer full-screen box: tapping outside the centered content dismisses.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            // Inner content: consumes clicks so they don't reach the dismiss handler.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {},
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Date above the image
                Text(
                    text = post.createdAt.toPostDate(),
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                )

                // Post image
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = post.carName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(375f / 468f)
                        .clip(RoundedCornerShape(20.dp)),
                    placeholder = painterResource(R.drawable.profile_picture),
                    error = painterResource(R.drawable.profile_picture),
                )

                // Like + comment row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Like
                    Row(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { if (!isLikeInFlight) onLikeToggle() },
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(
                                if (post.likedByCurrentUser) R.drawable.like_selected else R.drawable.like
                            ),
                            contentDescription = if (post.likedByCurrentUser) "Unlike" else "Like",
                            modifier = Modifier.size(26.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatCount(post.likeCount),
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Comment
                    Row(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onOpenComments,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.comment),
                            contentDescription = "Comments",
                            modifier = Modifier.size(26.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatCount(post.commentCount),
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }
    }
}

private fun formatCount(value: Long): String = when {
    value < 1_000 -> value.toString()
    value < 1_000_000 -> {
        val v = value / 1_000.0
        val s = String.format(Locale.US, "%.1f", v)
        "${s.trimEnd('0').trimEnd('.')}K"
    }
    else -> {
        val v = value / 1_000_000.0
        val s = String.format(Locale.US, "%.1f", v)
        "${s.trimEnd('0').trimEnd('.')}M"
    }
}
