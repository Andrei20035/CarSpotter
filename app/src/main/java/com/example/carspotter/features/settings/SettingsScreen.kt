package com.example.carspotter.features.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.carspotter.R
import com.example.carspotter.core.navigation.Screen
import com.example.carspotter.core.ui.theme.Poppins

private val BgTop = Color.Black
private val BgBottom = Color(0xFF080C30)
private val CardBg = Color(0x3DD9D9D9)          // rgba(217,217,217,0.24)
private val SectionLabelColor = Color(0xFF8D8D8D)
private val ItemTextColor = Color.White
private val LogoutRed = Color.Red

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.logoutCompleted) {
        if (uiState.logoutCompleted) {
            navController.navigate(Screen.Auth.route) {
                popUpTo(navController.graph.id) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 17.dp),
        ) {
            // ── Top bar ──────────────────────────────────────────────────────
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                )
            }

            // ── Profile card ─────────────────────────────────────────────────
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(122.dp)
                    .clip(RoundedCornerShape(21.dp))
                    .background(CardBg)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar
                    val avatarUrl = uiState.user?.profilePicturePath
                    if (avatarUrl.isNullOrBlank()) {
                        Image(
                            painter = painterResource(R.drawable.profile_picture),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape),
                        )
                    } else {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape),
                            placeholder = painterResource(R.drawable.profile_picture),
                            fallback = painterResource(R.drawable.profile_picture),
                            error = painterResource(R.drawable.profile_picture),
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = uiState.user?.username ?: "",
                            color = Color.White,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(21.dp))
                                .background(Color(0x80D9D9D9))
                                .border(1.dp, Color(0xE3FFFFFF), RoundedCornerShape(21.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { navController.navigate(Screen.EditProfile.route) },
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = "Edit Profile",
                                color = Color.White,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                            )
                        }
                    }
                }
            }

            // ── Account section ──────────────────────────────────────────────
            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel("Account")
            Spacer(modifier = Modifier.height(8.dp))
            SettingsRow(
                iconRes = R.drawable.user_icon,
                label = "Personal info",
                topRound = true,
                bottomRound = true,
                onClick = { navController.navigate(Screen.PersonalInfo.route) },
            )

            // ── Security section ─────────────────────────────────────────────
            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel("Security")
            Spacer(modifier = Modifier.height(8.dp))
            SettingsRow(
                iconRes = R.drawable.change_password,
                label = "Change password",
                topRound = true,
                bottomRound = true,
                onClick = { navController.navigate(Screen.ChangePassword.route) },
            )

            // ── Others section ───────────────────────────────────────────────
            Spacer(modifier = Modifier.height(24.dp))
            SectionLabel("Others")
            Spacer(modifier = Modifier.height(8.dp))
            SettingsRow(
                iconRes = R.drawable.privacy_policy,
                label = "Privacy Policy",
                topRound = true,
                bottomRound = false,
                onClick = { navController.navigate(Screen.PrivacyPolicy.route) },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.08f))
            )
            SettingsRow(
                iconRes = R.drawable.terms_conditions,
                label = "Terms & Conditions",
                topRound = false,
                bottomRound = true,
                onClick = { navController.navigate(Screen.TermsConditions.route) },
            )

            // ── Log out ──────────────────────────────────────────────────────
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = viewModel::logout,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.logout),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.isLoggingOut) "Logging out…" else "Log out",
                    color = LogoutRed,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = SectionLabelColor,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    )
}

@Composable
private fun SettingsRow(
    iconRes: Int,
    label: String,
    topRound: Boolean,
    bottomRound: Boolean,
    onClick: () -> Unit,
) {
    val topRadius = if (topRound) 20.dp else 0.dp
    val bottomRadius = if (bottomRound) 20.dp else 0.dp
    val shape = RoundedCornerShape(
        topStart = topRadius,
        topEnd = topRadius,
        bottomStart = bottomRadius,
        bottomEnd = bottomRadius,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape)
            .background(CardBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = ItemTextColor,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )
    }
}
