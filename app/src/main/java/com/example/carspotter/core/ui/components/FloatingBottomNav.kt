package com.example.carspotter.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class FeedNavItem { Home, Leaderboard, Activity, Profile }

/**
 * Floating, compact bottom navigation styled after the design's glassmorphism navbar.
 *
 * Note: Compose has no cheap real backdrop blur, so instead of a performance-heavy
 * RenderEffect we approximate the look with a semi-transparent dark/teal gradient fill,
 * a translucent light border, a subtle top highlight, and a moderate drop shadow.
 */
@Composable
fun FloatingBottomNav(
    selected: FeedNavItem,
    onHome: () -> Unit,
    onLeaderboard: () -> Unit,
    onPlus: () -> Unit,
    onActivity: () -> Unit,
    onProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 14.dp, shape = NavBarShape, clip = false)
                .clip(NavBarShape)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF123038).copy(alpha = 0.94f),
                            Color(0xFF0A1330).copy(alpha = 0.94f),
                        )
                    )
                )
                .border(width = 1.dp, brush = BorderBrush, shape = NavBarShape)
                .padding(horizontal = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavIcon(Icons.Filled.Home, "Home", selected == FeedNavItem.Home, onHome)
            NavIcon(Icons.Filled.Star, "Leaderboard", selected == FeedNavItem.Leaderboard, onLeaderboard)
            PlusButton(onPlus)
            NavIcon(Icons.Filled.Notifications, "Activity", selected == FeedNavItem.Activity, onActivity)
            NavIcon(Icons.Filled.Person, "Profile", selected == FeedNavItem.Profile, onProfile)
        }
    }
}

@Composable
private fun NavIcon(
    icon: ImageVector,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(64.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) Color(0xFF34D7C4) else Color.White.copy(alpha = 0.65f),
            modifier = Modifier.size(if (selected) 28.dp else 24.dp),
        )
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(elevation = 10.dp, shape = CircleShape, clip = false)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(listOf(Color(0xFF2DD4BF), Color(0xFF0E9F8E)))
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Post your find",
            tint = Color.White,
            modifier = Modifier.size(28.dp),
        )
    }
}

private val NavBarShape = RoundedCornerShape(32.dp)

private val BorderBrush = Brush.verticalGradient(
    listOf(
        Color.White.copy(alpha = 0.28f),
        Color.White.copy(alpha = 0.06f),
    )
)
