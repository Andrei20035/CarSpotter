package com.example.carspotter.features.feed

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.carspotter.R
import com.example.carspotter.core.ui.components.GradientText
import com.example.carspotter.core.navigation.Screen

private data class CurrentDayPost(
    @DrawableRes val imageRes: Int,
    val points: Int,
    val dateTime: String,
    val carName: String,
    val location: String,
)

private data class FeedPost(
    val username: String,
    @DrawableRes val avatarRes: Int,
    @DrawableRes val imageRes: Int,
    val carName: String,
    val location: String,
    val likes: Int,
    val comments: Int,
    val description: String,
)

@Composable
fun FeedScreen(
    navController: NavController,
) {
    val currentDayPosts = remember {
        listOf(
            CurrentDayPost(R.drawable.feed_image1, 220, "8 Feb 16:42", "Ferrari 488 Pista", "London, UK"),
            CurrentDayPost(R.drawable.feed_image2, 190, "8 Feb 18:11", "Lamborghini Aventador", "London, UK"),
            CurrentDayPost(R.drawable.feed_image3, 260, "9 Feb 09:14", "Porsche 911 GT3", "London, UK"),
            CurrentDayPost(R.drawable.feed_image4, 310, "9 Feb 12:55", "McLaren 720S", "London, UK"),
        )
    }

    var feedPosts by remember {
        mutableStateOf(
            listOf(
                FeedPost("alex_garage", R.drawable.profile_picture1, R.drawable.feed_image1, "Ferrari 488", "Milan, Italy", 47, 9, "Caught this beauty downtown before sunset."),
                FeedPost("nina.drive", R.drawable.profile_picture2, R.drawable.feed_image2, "Lamborghini Huracan", "Paris, France", 89, 14, "Sounded unreal in person."),
                FeedPost("vlad.spots", R.drawable.profile_picture, R.drawable.feed_image3, "Porsche GT3", "Berlin, Germany", 35, 6, "Clean spec, clean street."),
            )
        )
    }

    var showPostDialog by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { currentDayPosts.size })

    if (showPostDialog) {
        AlertDialog(
            onDismissRequest = { showPostDialog = false },
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
                TextButton(onClick = { showPostDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    Scaffold(
        topBar = {
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
                        text = "CarSpotter",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 30.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.profile_picture),
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { navController.navigate(Screen.Profile.route) }
                    )
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
                    .padding(top = 58.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                PostYourFindButton(onClick = { showPostDialog = true })
                Spacer(modifier = Modifier.height(28.dp))

                Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                    feedPosts.forEachIndexed { index, post ->
                        FeedPostCard(
                            post = post,
                            onToggleLike = {
                                feedPosts = feedPosts.toMutableList().also { list ->
                                    val old = list[index]
                                    val isLiked = old.likes % 2 == 1
                                    list[index] = old.copy(likes = if (isLiked) old.likes - 1 else old.likes + 1)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
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
private fun FeedPostCard(
    post: FeedPost,
    onToggleLike: () -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(post.avatarRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(post.username, color = Color.White, fontSize = 13.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_car),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("${post.carName},", color = Color.White, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_gps),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(post.location, color = Color.White, fontSize = 13.sp, maxLines = 1)
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
        ) {
            Image(
                painter = painterResource(post.imageRes),
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
                IconButton(onClick = onToggleLike) {
                    Image(
                        painter = painterResource(R.drawable.ic_like),
                        contentDescription = "Like",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(post.likes.toString(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(R.drawable.ic_comment),
                        contentDescription = "Comment",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(post.comments.toString(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = post.description,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
