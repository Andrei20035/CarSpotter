package com.example.carspotter.features.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carspotter.core.ui.theme.Poppins

private val CardFill = Color(0x613D3D3D)
private val CardBorder = Color(0xFF303030)
private val CardShape = RoundedCornerShape(12.dp)
private val WeeklyGreen = Color(0xFF28AB00)

@Composable
fun StatCard(
    title: String,
    value: Int,
    isWeeklyScore: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(181.dp)
            .height(109.dp)
            .clip(CardShape)
            .border(1.dp, CardBorder, CardShape)
            .background(CardFill)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )
        Text(
            text = if (isWeeklyScore) "+$value" else "$value",
            color = if (isWeeklyScore) WeeklyGreen else Color.White,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
        )
    }
}
