package com.example.carspotter.features.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.carspotter.R
import com.example.carspotter.core.ui.theme.Poppins
import com.example.carspotter.core.util.toRelativeTime
import com.example.carspotter.data.model.Comment
import com.example.carspotter.features.feed.CommentsSheetState

// Sheet palette — consistent with the dark feed surface.
private val SheetSurface = Color(0xFF11162E)
private val SheetAccent = Color(0xFF34D7C4)
private val TextPrimary = Color.White
private val TextSecondary = Color(0xB3FFFFFF) // white @ 70%
private val TextTertiary = Color(0x80FFFFFF)   // white @ 50%
private val FieldBorder = Color(0x1FFFFFFF)

/**
 * Instagram-style comments overlay: a modal bottom sheet with rounded top corners, a dark
 * background, a scrollable comments list, and an input row pinned at the bottom. Dismisses on
 * swipe-down or scrim tap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(
    state: CommentsSheetState,
    onDismiss: () -> Unit,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
    onRetry: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    // Reveal the newly posted comment (server appends oldest-first, so it lands at the bottom).
    LaunchedEffect(state.comments.size) {
        if (state.comments.isNotEmpty()) {
            listState.animateScrollToItem(state.comments.lastIndex)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SheetSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextTertiary) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
        ) {
            Text(
                text = "Comments",
                color = TextPrimary,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )

            Box(modifier = Modifier.weight(1f)) {
                when {
                    state.isLoading && state.comments.isEmpty() -> CenteredBox {
                        CircularProgressIndicator(color = SheetAccent)
                    }

                    state.errorMessage != null && state.comments.isEmpty() -> CenteredBox {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Couldn't load comments", color = TextSecondary, fontSize = 14.sp)
                            TextButton(onClick = onRetry) {
                                Text("Retry", color = SheetAccent, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    state.comments.isEmpty() -> CenteredBox {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No comments yet", color = TextSecondary, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Be the first to comment.", color = TextTertiary, fontSize = 13.sp)
                        }
                    }

                    else -> LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        items(state.comments, key = { it.id }) { comment ->
                            CommentRow(comment)
                            Spacer(modifier = Modifier.height(18.dp))
                        }
                    }
                }
            }

            CommentInputBar(
                draft = state.draft,
                canSend = state.canSend,
                isSubmitting = state.isSubmitting,
                onDraftChange = onDraftChange,
                onSend = onSend,
            )
        }
    }
}

@Composable
private fun CommentRow(comment: Comment) {
    Row(modifier = Modifier.fillMaxWidth()) {
        val avatarModifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
        if (comment.profilePictureUrl.isNullOrBlank()) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = avatarModifier,
            )
        } else {
            AsyncImage(
                model = comment.profilePictureUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = avatarModifier,
                placeholder = painterResource(R.drawable.profile_picture),
                fallback = painterResource(R.drawable.profile_picture),
                error = painterResource(R.drawable.profile_picture),
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.username,
                    color = TextPrimary,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                comment.createdAt?.let { createdAt ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = createdAt.toRelativeTime(),
                        color = TextTertiary,
                        fontFamily = Poppins,
                        fontSize = 11.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = comment.text,
                color = TextSecondary,
                fontFamily = Poppins,
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun CommentInputBar(
    draft: String,
    canSend: Boolean,
    isSubmitting: Boolean,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SheetSurface)
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Add a comment…", color = TextTertiary) },
            enabled = !isSubmitting,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontFamily = Poppins, fontSize = 14.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = SheetAccent,
                focusedBorderColor = SheetAccent,
                unfocusedBorderColor = FieldBorder,
            ),
        )

        IconButton(onClick = onSend, enabled = canSend) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = SheetAccent,
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send comment",
                    tint = if (canSend) SheetAccent else TextTertiary,
                )
            }
        }
    }
}

@Composable
private fun CenteredBox(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.Center) {
        content()
    }
}
