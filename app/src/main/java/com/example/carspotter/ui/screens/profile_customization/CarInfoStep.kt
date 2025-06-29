package com.example.carspotter.ui.screens.profile_customization

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
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
                            val carImageBytes = uriToByteArray(uiState.carImage, context)
                            viewModel.completeProfileSetup(profileImageBytes, carImageBytes)
                        }
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
}

fun uriToByteArray(imageSource: ImageSource?, context: Context): ByteArray? {
    return if (imageSource is ImageSource.Local) {
        context.contentResolver.openInputStream(imageSource.uri)?.use { it.readBytes() }
            ?: throw IOException("Unable to read image from URI")
    } else {
        null
    }
}
