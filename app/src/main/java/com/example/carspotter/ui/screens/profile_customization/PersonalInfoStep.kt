package com.example.carspotter.ui.screens.profile_customization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.carspotter.ui.screens.login.ScreenBackground

@Composable
fun PersonalInfoStep(
    uiState: ProfileCustomizationUiState,
    onAction: (ProfileCustomizationAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground {
            PictureContainer(
                picture = uiState.profileImage,
                text = "Your profile image",
            )
            PersonalInfoForm(
                uiState = uiState,
                onAction = { action ->
                    when(action) {
                        is ProfileCustomizationAction.UpdateProfileImage ->
                    }
                },
            )
        }
    }
}