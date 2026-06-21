package com.example.carspotter.features.feed.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carspotter.core.ui.components.shimmer

/**
 * Visual-only loading placeholder that mirrors the structure and spacing of a real feed post
 * (see `FeedPostCard`): avatar + username/location, the large image card, the like/comment row,
 * and a caption line — each filled with a [shimmer]. Purely presentational; holds no data.
 *
 * Spacing/shape values intentionally match the real card so the layout doesn't shift when actual
 * posts replace the skeletons.
 */
@Composable
fun FeedPostSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // ---- Header: avatar · username/location · options ----
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(37.dp).shimmer(CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.height(13.dp).width(96.dp).shimmer())
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.height(11.dp).width(150.dp).shimmer())
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(9.dp))

        // ---- Main image card (same 375×468 aspect + 18dp radius as the real post) ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(375f / 468f)
                .shimmer(RoundedCornerShape(18.dp))
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ---- Engagement row (like icon + count · comment icon + count), indented like the real one ----
        Row(
            modifier = Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth(0.4f)
                    .shimmer()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---- Caption line ----
        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .height(12.dp)
                .fillMaxWidth(0.6f)
                .shimmer()
        )
    }
}
