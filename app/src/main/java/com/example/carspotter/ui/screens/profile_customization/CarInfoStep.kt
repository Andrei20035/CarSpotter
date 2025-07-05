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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.carspotter.ui.components.CustomSnackbar
import com.example.carspotter.ui.navigation.Screen
import com.example.carspotter.ui.screens.login.ScreenBackground
import java.io.IOException
import kotlin.toString

@Composable
fun CarInfoStep(
    viewModel: ProfileCustomizationViewModel,
    navController: NavController,
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


    val context = LocalContext.current

    LaunchedEffect(uiState.isUserCreated) {
        if (uiState.isUserCreated) {
            navController.navigate(Screen.Feed.route) {
                popUpTo(Screen.ProfileCustomization.route) { inclusive = true }
            }
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
                CarInfoForm(
                    uiState = uiState,
                    onAction = { action ->
                        when (action) {
                            is ProfileCustomizationAction.UpdateCarImage -> viewModel.updateCarImage(
                                action.imageSource
                            )

                            is ProfileCustomizationAction.UpdateCarBrand -> viewModel.updateCarBrand(
                                action.brand
                            )

                            is ProfileCustomizationAction.UpdateCarModel -> viewModel.updateCarModel(
                                action.model
                            )

                            is ProfileCustomizationAction.Complete -> {
                                val profileImageBytes =
                                    uriToByteArray(uiState.profilePicture, context)
                                val carImageBytes = uriToByteArray(uiState.carPicture, context)
                                val profileImageMime =
                                    (uiState.profilePicture as? ImageSource.Local)?.mimeType
                                val carImageMime =
                                    (uiState.carPicture as? ImageSource.Local)?.mimeType
                                viewModel.completeProfileSetup(
                                    profileImageBytes, profileImageMime, carImageBytes, carImageMime
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
}

@Composable
private fun CarInfoForm(
    uiState: ProfileCustomizationUiState,
    onAction: (ProfileCustomizationAction) -> Unit,
) {
    val context = LocalContext.current
    var brandDropdownExpanded by remember { mutableStateOf(false) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 30.dp),
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
            DropdownFieldWithoutOverlay(
                modifier = Modifier.padding(bottom = 16.dp),
                selectedItem = uiState.selectedBrand,
                label = "Brand",
                onDropdownToggle = {
                    brandDropdownExpanded = !brandDropdownExpanded
                    Log.d("brandDropdownExpanded", brandDropdownExpanded.toString())
                    if (brandDropdownExpanded) modelDropdownExpanded = false
                }
            )
            DropdownFieldWithoutOverlay(
                selectedItem = uiState.selectedModel,
                label = "Model",
                onDropdownToggle = {
                    modelDropdownExpanded = !modelDropdownExpanded
                    Log.d("modelDropdownExpanded", modelDropdownExpanded.toString())
                    // Close brand dropdown if model is opened
                    if (modelDropdownExpanded) brandDropdownExpanded = false
                }
            )
            Spacer(Modifier.weight(1f))
            NextStepButton(
                text = "Next",
                onClick = { onAction(ProfileCustomizationAction.Complete) },
            )
            Spacer(Modifier.height(8.dp))
            SkipCarInfoText(
                onClick = { onAction(ProfileCustomizationAction.Complete) },
            )

        }

        DropdownOverlay(
            visible = brandDropdownExpanded,
            items = uiState.allBrands,
            onItemSelected = { brand ->
                onAction(ProfileCustomizationAction.UpdateCarBrand(brand))
                brandDropdownExpanded = false
            },
            onDismiss = { brandDropdownExpanded = false }
        )

        DropdownOverlay(
            visible = modelDropdownExpanded,
            items = uiState.modelsForSelectedBrand,
            onItemSelected = { model ->
                onAction(ProfileCustomizationAction.UpdateCarModel(model))
                modelDropdownExpanded = false
            },
            onDismiss = { modelDropdownExpanded = false }
        )
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
