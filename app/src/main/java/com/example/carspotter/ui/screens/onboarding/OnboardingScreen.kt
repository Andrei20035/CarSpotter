package com.example.carspotter.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carspotter.R
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    onComplete: () -> Unit = {
        navController.navigate("login") {
            popUpTo("onboarding") {
                inclusive = true
            }
        }
    }
) {
    val pages = remember {
        listOf(
            OnboardingPage(
                title = "Welcome to",
                subtitle = "CarSpotter!",
                imageRes = R.drawable.app_presentation_1,
                backgroundColor = Color(0xFF1A1A1A)
            ),
            OnboardingPage(
                title = "Spot the Unseen",
                subtitle = "Uncover hidden gems on the streets and discover rare automotive treasures",
                imageRes = R.drawable.app_presentation_2,
                backgroundColor = Color(0xFF2C1810)
            ),
            OnboardingPage(
                title = "Capture the Moment",
                subtitle = "Snap and share your automotive encounters with fellow enthusiasts",
                imageRes = R.drawable.app_presentation_3,
                backgroundColor = Color(0xFF1A2C1A)
            ),
            OnboardingPage(
                title = "Become a Top Spotter 🌟",
                subtitle = "Engage in challenges, earn badges, and climb the leaderboard",
                imageRes = R.drawable.app_presentation_4,
                backgroundColor = Color(0xFF1A1A2C)
            )
        )
    }

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                coroutineScope.launch {
                    if (pagerState.currentPage < pages.lastIndex) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onComplete()
                    }
                }
            }
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            OnboardingPageContent(pageData = pages[page])
        }

        TextButton(
            onClick = { onComplete() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "Skip",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
        // Progress indicator at top
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(if (index <= pagerState.currentPage) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (index <= pagerState.currentPage)
                                Color.White
                            else
                                Color.White.copy(alpha = 0.3f)
                        )
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            activeColor = Color.White,
            inactiveColor = Color.White.copy(alpha = 0.3f),
            indicatorWidth = 8.dp,
            indicatorHeight = 8.dp,
            spacing = 8.dp
        )

        // Tap instruction
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = if (pagerState.currentPage < pages.lastIndex)
                    "Tap anywhere to continue"
                else
                    "Tap to get started",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OnboardingPageContent(pageData: OnboardingPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageData.backgroundColor)
    ) {
        // Background image
        Image(
            painter = painterResource(id = pageData.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.8f
        )

        // Gradient overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Title
            Text(
                text = pageData.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = pageData.subtitle,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}