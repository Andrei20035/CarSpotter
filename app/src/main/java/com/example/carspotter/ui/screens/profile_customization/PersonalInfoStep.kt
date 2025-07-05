package com.example.carspotter.ui.screens.profile_customization

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.carspotter.ui.components.CustomSnackbar
import com.example.carspotter.ui.screens.login.ScreenBackground

@Composable
fun PersonalInfoStep(
    viewModel: ProfileCustomizationViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            Log.d("ERROR MESSAGE", uiState.errorMessage.toString())
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                CustomSnackbar(data.visuals.message)
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ScreenBackground {
                PersonalInfoForm(
                    uiState = uiState,
                    onAction = { action ->
                        when (action) {
                            is ProfileCustomizationAction.UpdateProfileImage -> viewModel.updateProfileImage(
                                action.imageSource
                            )

                            is ProfileCustomizationAction.UpdateFullName -> viewModel.updateFullName(
                                action.fullName
                            )

                            is ProfileCustomizationAction.UpdateUsername -> viewModel.updateUsername(
                                action.username
                            )

                            is ProfileCustomizationAction.UpdateBirthDate -> viewModel.updateBirthDate(
                                action.birthDate
                            )

                            is ProfileCustomizationAction.UpdateCountry -> viewModel.updateCountry(
                                action.country
                            )

                            is ProfileCustomizationAction.NextStep -> viewModel.nextStep()
                            else -> {}
                        }
                    },
                )
            }
            if (uiState.isFetchingBrands) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun PersonalInfoForm(
    uiState: ProfileCustomizationUiState,
    onAction: (ProfileCustomizationAction) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PictureContainer(
            currentStep = uiState.currentStep,
            picture = uiState.profilePicture,
            text = "Your profile picture",
            onImageSelected = { uri ->
                val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                onAction(
                    ProfileCustomizationAction.UpdateProfileImage(
                        ImageSource.Local(
                            uri,
                            mimeType
                        )
                    )
                )
            }
        )
        LabeledTextField(
            label = "Full name",
            value = uiState.fullName,
            onValueChange = { onAction(ProfileCustomizationAction.UpdateFullName(it)) },
            placeholderText = "Josh Michael"
        )
        LabeledTextField(
            label = "Username",
            value = uiState.username,
            onValueChange = { onAction(ProfileCustomizationAction.UpdateUsername(it)) },
            placeholderText = "Josh94"
        )
        BirthDateField(
            birthDate = uiState.birthDate,
            onBirthDateChanged = { onAction(ProfileCustomizationAction.UpdateBirthDate(it)) }
        )
        CountryDropdown(
            selectedCountry = uiState.country,
            onCountrySelected = { onAction(ProfileCustomizationAction.UpdateCountry(it.name)) }
        )

        Spacer(Modifier.weight(1f))

        NextStepButton(
            text = "Next",
            onClick = { onAction(ProfileCustomizationAction.NextStep) },
        )
    }
}