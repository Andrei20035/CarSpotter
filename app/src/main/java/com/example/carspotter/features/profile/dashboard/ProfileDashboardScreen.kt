package com.example.carspotter.features.profile.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.carspotter.R
import com.example.carspotter.core.navigation.Screen

@Composable
fun ProfileDashboardScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black,
                                Color.Black.copy(alpha = 0.78f),
                                Color.Black.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("alex_garage", color = Color.White, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF05081D)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(Brush.verticalGradient(listOf(Color.Black, Color(0xFF05081D))))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box {
                    Image(
                        painter = painterResource(R.drawable.user_car_placeholder),
                        contentDescription = "Car",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    )

                    Image(
                        painter = painterResource(R.drawable.profile_picture),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 184.dp, start = 12.dp)
                            .size(140.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(90.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Alex", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 28.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Italy", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Text(
                        "Expert Spotter",
                        color = Color(0xFFD96570),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    SpotScoreCard()

                    Spacer(modifier = Modifier.height(16.dp))
                    LeaderboardCard()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SpotScoreCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFFD96570), Color(0xFFA470BE))))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("SpotScore", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Text("1280", color = Color.White, fontSize = 54.sp, fontWeight = FontWeight.Bold)
                    Text("220 until next rank", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }

                CircularProgressBadge(progress = 0.72f)
            }
        }
    }
}

@Composable
private fun CircularProgressBadge(progress: Float) {
    Box(
        modifier = Modifier
            .size(92.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.22f)),
        contentAlignment = Alignment.Center
    ) {
        Text("${(progress * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun LeaderboardCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.leaderboard_illustration),
            contentDescription = "Leaderboard",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.10f))
                .padding(top = 12.dp, start = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Leaderboard", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Check your rank within the car-spotting community.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.width(220.dp)
            )
        }
    }
}
