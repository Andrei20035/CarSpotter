package com.example.carspotter.ui.screens.profile_customization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        FullNameField(
            fullName = uiState.fullName,
            onFullNameChanged = { onAction(ProfileCustomizationAction.UpdateFullName(it)) }
        )
        UsernameField(
            username = uiState.username,
            onUsernameChange =  { onAction(ProfileCustomizationAction.UpdateUsername(it)) }
        )
        BirthDateField(
            birthDate = uiState.birthDate,
            onBirthDateChanged = { onAction(ProfileCustomizationAction.UpdateBirthDate(it)) }
        )
        CountryDropdown(
            selectedCountry = uiState.country,
            onCountrySelected = { onAction(ProfileCustomizationAction.UpdateCountry(it)) }
        )

        Spacer(Modifier.height(52.dp))

        Button(
            onClick = { onAction(ProfileCustomizationAction.NextStep) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF0AB25),
                disabledContainerColor = Color(0xFFF0AB25).copy(alpha = 0.7f),
                disabledContentColor = Color.Black.copy(alpha = 0.7f)
            ),
        ) {
            Text(
                text = "Next",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}