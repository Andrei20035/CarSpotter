package com.example.carspotter.ui.screens.profile_customization

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.carspotter.ui.screens.login.ScreenBackground

@Composable
fun PersonalInfoStep(
    viewModel: ProfileCustomizationViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground {
            PersonalInfoForm(
                uiState = uiState,
                onAction = { action ->
                    when (action) {
                        is ProfileCustomizationAction.UpdateProfileImage -> viewModel.updateProfileImage(action.imageSource)
                        is ProfileCustomizationAction.UpdateFullName -> viewModel.updateFullName(action.fullName)
                        is ProfileCustomizationAction.UpdateUsername -> viewModel.updateUsername(action.username)
                        is ProfileCustomizationAction.UpdateBirthDate -> viewModel.updateBirthDate(action.birthDate)
                        is ProfileCustomizationAction.UpdateCountry -> viewModel.updateCountry(action.country)
                        is ProfileCustomizationAction.NextStep -> viewModel.nextStep()
                        else -> {}
                    }
                },
            )
        }
    }
}

@Composable
private fun PersonalInfoForm(
    uiState: ProfileCustomizationUiState,
    onAction: (ProfileCustomizationAction) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PictureContainer(
            picture = uiState.profileImage,
            text = "Your profile picture",
            onImageSelected = { uri ->
                onAction(ProfileCustomizationAction.UpdateProfileImage(ImageSource.Local(uri)))
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

        Spacer(Modifier.height(52.dp))

        NextStepButton(
            text = "Next",
            onClick = { onAction(ProfileCustomizationAction.NextStep) },
        )
    }
}