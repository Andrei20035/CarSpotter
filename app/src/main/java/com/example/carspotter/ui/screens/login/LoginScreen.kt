package com.example.carspotter.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carspotter.ui.components.GradientText
import com.example.carspotter.ui.navigation.Screen
import kotlinx.coroutines.delay

/**
 * Login screen that allows users to sign in or create a new account.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var shouldShowConfirm by remember { mutableStateOf(false) }
    var shouldShowForgot by remember { mutableStateOf(false) }

    val cardHeight by animateDpAsState(
        targetValue = if(uiState.isLoginMode) 480.dp else 620.dp,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "cardHeight"
    )

    // Handle navigation when authenticated
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    // Background and main layout
    LoginScreenBackground {
        // App title and tagline
        LoginHeader()

        // Main card with auth form
        LoginCard(
            cardHeight = cardHeight,
            uiState = uiState,
            shouldShowConfirm = shouldShowConfirm,
            shouldShowForgot = shouldShowForgot,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
            onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
            onLogin = viewModel::login,
            onSignUp = viewModel::signUp,
            onGoogleSignIn = viewModel::googleSignIn,
            onForgotPassword = viewModel::forgotPassword,
            onToggleMode = viewModel::toggleLoginMode
        )
    }

    // Handle animations for showing/hiding confirm password field
    LaunchedEffect(uiState.isLoginMode) {
        if (!uiState.isLoginMode) {
            delay(300L)
            shouldShowConfirm = true
        } else {
            shouldShowConfirm = false
        }
    }

    // Handle animations for showing/hiding forgot password text
    LaunchedEffect(uiState.isLoginMode) {
        if (uiState.isLoginMode) {
            delay(260L)
            shouldShowForgot = true
        } else {
            shouldShowForgot = false
        }
    }
}

/**
 * Background for the login screen with gradient.
 */
@Composable
private fun LoginScreenBackground(
    content: @Composable () -> Unit
) {
    val gradientColors = listOf(Color(0xFF000000), Color(0xFF080C30))
    val stops = listOf(0.0f, 0.35f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = stops.zip(gradientColors).toTypedArray(),
                    startY = 0.0f,
                    endY = Float.POSITIVE_INFINITY,
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

/**
 * Header with app title and tagline.
 */
@Composable
private fun LoginHeader() {
    Spacer(modifier = Modifier.height(80.dp))

    GradientText(
        text = "CarSpotter",
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
    )

    Text(
        text = "Spot. Snap. Share.",
        color = Color.White.copy(alpha = 0.8f),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(80.dp))
}

/**
 * Main card containing the login/signup form.
 */
@Composable
private fun LoginCard(
    cardHeight: androidx.compose.ui.unit.Dp,
    uiState: LoginUiState,
    shouldShowConfirm: Boolean,
    shouldShowForgot: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onToggleMode: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email field
            EmailField(
                email = uiState.email,
                onEmailChange = onEmailChange
            )

            // Password field
            PasswordField(
                password = uiState.password,
                onPasswordChange = onPasswordChange,
                isPasswordVisible = uiState.isPasswordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                imeAction = if (uiState.isLoginMode) ImeAction.Done else ImeAction.Next
            )

            // Confirm password field (only in sign up mode)
            AnimatedVisibility(
                visible = shouldShowConfirm,
                enter = fadeIn(
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ),
                exit = fadeOut(
                    animationSpec = tween(10, easing = FastOutSlowInEasing)
                )
            ) {
                PasswordField(
                    password = uiState.confirmPassword,
                    onPasswordChange = onConfirmPasswordChange,
                    isPasswordVisible = uiState.isConfirmPasswordVisible,
                    onTogglePasswordVisibility = onToggleConfirmPasswordVisibility,
                    label = "Confirm Password",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Forgot password text (only in login mode)
            AnimatedVisibility(
                visible = shouldShowForgot,
                enter = fadeIn(
                    animationSpec = tween(200, easing = FastOutSlowInEasing)
                ),
                exit = fadeOut(
                    animationSpec = tween(200, easing = FastOutSlowInEasing)
                )
            ) {
                ForgotPasswordText(onForgotPasswordClick = onForgotPassword)
            }

            if (!uiState.isLoginMode) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(Modifier.weight(1f))

            // Primary action button (Login/Sign Up)
            PrimaryActionButton(
                text = if (uiState.isLoginMode) "Log In" else "Sign Up",
                onClick = if (uiState.isLoginMode) onLogin else onSignUp,
                isLoading = uiState.isLoading
            )

            // Google sign in button
            GoogleSignInButton(
                text = if (uiState.isLoginMode) "Log In with Google" else "Sign Up with Google",
                onClick = onGoogleSignIn,
                isLoading = uiState.isLoading
            )

            // Switch between login and sign up
            AuthModeSwitchText(
                isLoginMode = uiState.isLoginMode,
                onToggleMode = onToggleMode
            )
        }
    }
}
