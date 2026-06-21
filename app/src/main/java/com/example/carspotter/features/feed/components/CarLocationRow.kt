package com.example.carspotter.features.feed.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.carspotter.R
import com.example.carspotter.core.ui.theme.Poppins

// Same dark/glass palette as the post-options dropdown, so the details popover matches it.
private val PopoverSurface = Color(0xFF1B1F33)
private val PopoverBorder = Color(0x1FFFFFFF) // white @ ~12%

// Icon sizes mirror the inline row exactly (ic_car 14dp, ic_gps 15dp) — visual style unchanged.
private val CarIconSize = 14.dp
private val GpsIconSize = 15.dp
private val RowTextSize = 13.3.sp
// Width of the right-edge alpha fade applied only when the row overflows.
private val FadeWidth = 24.dp

/**
 * The post header's second row — `ic_car · car name · ic_gps · location` — kept on a single line.
 *
 * Overflow handling:
 *  - The car name takes priority: it's measured greedily, while the location gets the leftover
 *    space (and therefore truncates first).
 *  - When the row can't fit, a subtle right-edge alpha fade masks the overflow instead of a hard
 *    cut. The fade is applied *only* while something actually overflows — never when it all fits.
 *
 * Tapping the row opens a compact details popover (full car name + full location), styled like the
 * post-options dropdown and dismissed by tapping outside. Layout/visual style of the row itself is
 * unchanged.
 */
@Composable
fun CarLocationRow(
    carName: String,
    location: String?,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    // Per-text overflow flags, fed by onTextLayout. Reset when the underlying text changes.
    var carOverflow by remember(carName) { mutableStateOf(false) }
    var locationOverflow by remember(location) { mutableStateOf(false) }
    val showFade = carOverflow || locationOverflow
    val popoverOffsetY = with(LocalDensity.current) { 22.dp.roundToPx() }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { expanded = true },
                )
                .rightEdgeFade(visible = showFade, fadeWidth = FadeWidth),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_car),
                contentDescription = null,
                modifier = Modifier.size(CarIconSize),
            )
            Spacer(modifier = Modifier.width(5.dp))
            // No weight → measured greedily, so the car name keeps priority over the location.
            Text(
                // Design: "Porsche 911," — trailing comma when a location follows.
                text = if (location != null) "$carName," else carName,
                color = Color.White,
                fontSize = RowTextSize,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                onTextLayout = { carOverflow = it.hasVisualOverflow },
            )
            // Location resolved from the post's coordinates (town, country). Hidden when the backend
            // hasn't geocoded the post yet — never fabricated.
            if (location != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Image(
                    painter = painterResource(R.drawable.ic_gps),
                    contentDescription = null,
                    modifier = Modifier.size(GpsIconSize),
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = location,
                    color = Color.White,
                    fontSize = RowTextSize,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip,
                    onTextLayout = { locationOverflow = it.hasVisualOverflow },
                    // Weighted → takes only the leftover space and truncates before the car name.
                    modifier = Modifier.weight(1f),
                )
            }
        }

        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, popoverOffsetY),
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true),
            ) {
                CarDetailsPopover(carName = carName, location = location)
            }
        }
    }
}

/** Compact full-details popover: full car name + full location, in the post-options glass style. */
@Composable
private fun CarDetailsPopover(carName: String, location: String?) {
    Column(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(PopoverSurface)
            .border(1.dp, PopoverBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DetailRow(iconRes = R.drawable.ic_car, iconSize = 16.dp, text = carName)
        if (location != null) {
            DetailRow(iconRes = R.drawable.ic_gps, iconSize = 17.dp, text = location)
        }
    }
}

@Composable
private fun DetailRow(iconRes: Int, iconSize: Dp, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
        )
        Spacer(modifier = Modifier.width(10.dp))
        // Full, untruncated text — wraps within the compact max width for very long values.
        Text(
            text = text,
            color = Color.White,
            fontFamily = Poppins,
            fontSize = 13.3.sp,
        )
    }
}

/**
 * Masks the right edge with a horizontal alpha gradient (content fades to transparent) — but only
 * when [visible]. Uses an offscreen layer + [BlendMode.DstIn] so the fade affects this content only.
 * A no-op when nothing overflows, so the row never shows a phantom fade while it fits.
 */
private fun Modifier.rightEdgeFade(visible: Boolean, fadeWidth: Dp): Modifier {
    if (!visible) return this
    return this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            val fadePx = fadeWidth.toPx()
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black, Color.Transparent),
                    startX = size.width - fadePx,
                    endX = size.width,
                ),
                blendMode = BlendMode.DstIn,
            )
        }
}
