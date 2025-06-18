package com.example.carspotter.ui.screens.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carspotter.BuildConfig
import com.example.carspotter.domain.model.AuthProvider
import com.example.carspotter.ui.components.GradientText
import com.example.carspotter.ui.navigation.Screen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay

/**
 * Login screen that allows users to sign in or create a new account.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Box(modifier= Modifier.fillMaxSize()) {
        ScreenBackground {
            LoginHeader()
            LoginCard(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is LoginAction.EmailChanged -> viewModel.updateEmail(action.email)
                        is LoginAction.PasswordChanged -> viewModel.updatePassword(action.password)
                        is LoginAction.ConfirmPasswordChanged -> viewModel.updateConfirmPassword(action.password)
                        is LoginAction.TogglePasswordVisibility -> viewModel.togglePasswordVisibility()
                        is LoginAction.ToggleConfirmPasswordVisibility -> viewModel.toggleConfirmPasswordVisibility()
                        is LoginAction.Login -> {
                            viewModel.setProviderAndToken(action.googleId, action.provider)
                            Log.d(
                                "GOOGLE_ID and PROVIDER",
                                "googleId: ${uiState.googleIdToken}, provider: ${uiState.provider}"
                            )
                            viewModel.login(action.googleId)
                        }

                        is LoginAction.SignUp -> {
                            viewModel.setProviderAndToken(null, AuthProvider.REGULAR)
                            viewModel.signUp()
                        }

                        is LoginAction.ForgotPassword -> viewModel.forgotPassword()
                        is LoginAction.ToggleMode -> viewModel.toggleLoginMode()
                        is LoginAction.ResetOnboarding -> {
                            viewModel.resetOnboardingStatus {
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        }
                    }
                }
            )
        }
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ScreenBackground(
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

@Composable
private fun LoginCard(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit
) {
    var shouldShowConfirm by remember { mutableStateOf(false) }
    var shouldShowForgot by remember { mutableStateOf(false) }

    val cardHeight by animateDpAsState(
        targetValue = if (uiState.isLoginMode) 480.dp else 620.dp,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "cardHeight"
    )

    LaunchedEffect(uiState.isLoginMode) {
        if (!uiState.isLoginMode) {
            delay(300L)
            shouldShowConfirm = true
        } else {
            shouldShowConfirm = false
        }
    }

    LaunchedEffect(uiState.isLoginMode) {
        if (uiState.isLoginMode) {
            delay(260L)
            shouldShowForgot = true
        } else {
            shouldShowForgot = false
        }
    }

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
            LoginForm(
                uiState = uiState,
                shouldShowConfirm = shouldShowConfirm,
                shouldShowForgot = shouldShowForgot,
                onAction = onAction
            )

            Spacer(Modifier.weight(1f))

            LoginActions(
                uiState = uiState,
                onAction = onAction
            )

            LoginFooter(
                uiState = uiState,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun LoginForm(
    uiState: LoginUiState,
    shouldShowConfirm: Boolean,
    shouldShowForgot: Boolean,
    onAction: (LoginAction) -> Unit
) {
    EmailField(
        email = uiState.email,
        onEmailChange = { onAction(LoginAction.EmailChanged(it)) }
    )

    PasswordField(
        password = uiState.password ?: "",
        onPasswordChange = { onAction(LoginAction.PasswordChanged(it)) },
        isPasswordVisible = uiState.isPasswordVisible,
        onTogglePasswordVisibility = { onAction(LoginAction.TogglePasswordVisibility) },
        imeAction = if (uiState.isLoginMode) ImeAction.Done else ImeAction.Next
    )

    AnimatedVisibility(
        visible = shouldShowConfirm,
        enter = fadeIn(animationSpec = tween(300, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(10, easing = FastOutSlowInEasing))
    ) {
        PasswordField(
            password = uiState.confirmPassword,
            onPasswordChange = { onAction(LoginAction.ConfirmPasswordChanged(it)) },
            isPasswordVisible = uiState.isConfirmPasswordVisible,
            onTogglePasswordVisibility = { onAction(LoginAction.ToggleConfirmPasswordVisibility) },
            label = "Confirm Password",
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    AnimatedVisibility(
        visible = shouldShowForgot,
        enter = fadeIn(animationSpec = tween(200, easing = FastOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
    ) {
        ForgotPasswordText(
            onForgotPasswordClick = { onAction(LoginAction.ForgotPassword) }
        )
    }

    val context = LocalContext.current

//    AnimatedVisibility(
//        visible = uiState.errorMessage != null,
//        enter = fadeIn(animationSpec = tween(200, easing = FastOutSlowInEasing)),
//        exit = fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing))
//    ) {
//        uiState.errorMessage?.let { errorMessage ->
//            Text(
//                text = errorMessage,
//                color = Color.Red,
//                fontSize = 14.sp,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 8.dp),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    if (!uiState.isLoginMode) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LoginActions(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit
) {
    PrimaryActionButton(
        text = if (uiState.isLoginMode) "Log In" else "Sign Up",
        onClick = {
            if (uiState.isLoginMode) {
                onAction(LoginAction.Login(null, AuthProvider.REGULAR))
            } else {
                onAction(LoginAction.SignUp(null, AuthProvider.REGULAR))
            }
        },
        isLoading = uiState.isLoading
    )

    GoogleSignInHandler(
        text = if (uiState.isLoginMode) "Log In with Google" else "Sign Up with Google",
        isLoading = uiState.isLoading,
        onGoogleSignIn = { idToken, email ->
            Log.d("TOKEN_ID", "Token received in GoogleSignInHandler: $idToken")
            onAction(LoginAction.EmailChanged(email))
            onAction(LoginAction.Login(idToken, AuthProvider.GOOGLE))
        }
    )
}

@Composable
private fun LoginFooter(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit
) {
    Row {
        // For testing: Reset onboarding
        Text(
            text = "Reset ",
            color = Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onAction(LoginAction.ResetOnboarding) }
        )

        AuthModeSwitchText(
            isLoginMode = uiState.isLoginMode,
            onToggleMode = { onAction(LoginAction.ToggleMode) }
        )
    }
}

@Composable
private fun GoogleSignInHandler(
    text: String,
    isLoading: Boolean,
    onGoogleSignIn: (String?, email: String?) -> Unit
) {
    val context = LocalContext.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, gso)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            val email = account.email
            onGoogleSignIn(idToken, email)
            Log.d("GOOGLE_SIGN_IN", "Web Client ID: ${BuildConfig.WEB_CLIENT_ID}")
            Log.d("GOOGLE_TOKEN_ID", "idToken: $idToken")
        } catch (e: ApiException) {
            Log.e("GOOGLE_SIGN_IN", "Sign-in failed", e)
            onGoogleSignIn(null, null)
        }
    }



    GoogleSignInButton(
        text = text,
        onClick = {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        },
        isLoading = isLoading
    )
}