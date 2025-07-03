package com.example.carspotter.ui.screens.profile_customization

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carspotter.ui.navigation.Screen
import com.example.carspotter.ui.screens.login.ScreenBackground
import java.io.IOException

@Composable
fun CarInfoStep(
    viewModel: ProfileCustomizationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiState.isUserCreated) {
        if (uiState.isUserCreated) {
            navController.navigate(Screen.Feed.route) {
                popUpTo(Screen.ProfileCustomization.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Log.d("ERROR MESSAGE", uiState.errorMessage.toString())
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ScreenBackground {
            CarInfoForm(
                uiState = uiState,
                onAction = { action ->
                    when(action) {
                        is ProfileCustomizationAction.UpdateCarImage -> viewModel.updateCarImage(action.imageSource)
                        is ProfileCustomizationAction.UpdateCarBrand -> viewModel.updateCarBrand(action.brand)
                        is ProfileCustomizationAction.UpdateCarModel -> viewModel.updateCarModel(action.model)
                        is ProfileCustomizationAction.Complete ->  {
                            val profileImageBytes = uriToByteArray(uiState.profilePicture, context)
                            val carImageBytes = uriToByteArray(uiState.carPicture, context)
                            val profileImageMime = (uiState.profilePicture as? ImageSource.Local)?.mimeType
                            val carImageMime = (uiState.carPicture as? ImageSource.Local)?.mimeType
                            viewModel.completeProfileSetup(profileImageBytes, profileImageMime, carImageBytes, carImageMime
                            )
                        }
                        is ProfileCustomizationAction.PreviousStep -> viewModel.previousStep()
                        else -> {}
                    }
                }
            )
        }
    }
}

@Composable
private fun CarInfoForm(
    uiState: ProfileCustomizationUiState,
    onAction: (ProfileCustomizationAction) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp, horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PictureContainer(
                currentStep = uiState.currentStep,
                picture = uiState.carPicture,
                text = "You car picture",
                onImageSelected = { uri ->
                    val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                    onAction(ProfileCustomizationAction.UpdateCarImage(ImageSource.Local(uri, mimeType)))
                },
                onBackPress = { onAction(ProfileCustomizationAction.PreviousStep) }
            )
            Spacer(Modifier.height(32.dp))
            DropdownField(
                modifier = Modifier.padding(bottom = 16.dp),
                selectedItem = uiState.selectedBrand,
                items = uiState.allBrands,
                label = "Brand",
                onItemSelected = { brand ->
                    onAction(ProfileCustomizationAction.UpdateCarBrand(brand))
                }
            )
            DropdownField(
                selectedItem = uiState.selectedModel,
                items = uiState.modelsForSelectedBrand,
                label = "Model",
                onItemSelected = { model ->
                    onAction(ProfileCustomizationAction.UpdateCarModel(model))
                }
            )
            Spacer(Modifier.weight(1f))
            NextStepButton(
                text = "Next",
                onClick = { onAction(ProfileCustomizationAction.Complete) },
            )
            Spacer(Modifier.height(8.dp))
            SkipCarInfoText(
                onClick = { onAction(ProfileCustomizationAction.NextStep) },
            )

        }

        if (uiState.isFetchingBrands || uiState.isFetchingModels) {
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

fun uriToByteArray(imageSource: ImageSource?, context: Context): ByteArray? {
    return if (imageSource is ImageSource.Local) {
        context.contentResolver.openInputStream(imageSource.uri)?.use { it.readBytes() }
            ?: throw IOException("Unable to read image from URI")
    } else {
        null
    }
}
