package com.example.carspotter.ui.screens.profile_customization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.remote.model.user.CreateUserRequest
import com.example.carspotter.data.remote.model.user.UpdateProfilePictureRequest
import com.example.carspotter.data.remote.model.user.UploadImageRequest
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.remote.model.user_car.UserCarUpdateRequest
import com.example.carspotter.data.repository.CarModelRepository
import com.example.carspotter.data.repository.UserCarRepository
import com.example.carspotter.data.repository.UserRepository
import com.example.carspotter.utils.ApiResult
import com.example.carspotter.utils.uploadToS3
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileCustomizationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userCarRepository: UserCarRepository,
    private val userPreferences: UserPreferences,
    private val carModelRepository: CarModelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileCustomizationUiState())
    val uiState: StateFlow<ProfileCustomizationUiState> = _uiState.asStateFlow()

    fun updateProfileImage(imageSource: ImageSource?) {
        _uiState.update { it.copy(profilePicture = imageSource) }
    }

    fun updateFullName(fullName: String) {
        _uiState.update { it.copy(fullName = fullName) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateBirthDate(birthDate: LocalDate) {
        _uiState.update { it.copy(birthDate = birthDate) }
    }

    fun updateCountry(country: String) {
        _uiState.update { it.copy(country = country) }
    }

    fun updateCarImage(imageSource: ImageSource?) {
        _uiState.update { it.copy(carImage = imageSource) }
    }

    fun updateCarBrand(brand: String) {
        _uiState.update { it.copy(selectedBrand = brand) }
    }

    fun updateCarModel(model: String) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    suspend fun fetchCarBrands() {
        _uiState.update { it.copy( isFetchingBrands = true ) }
        val result = carModelRepository.getAllCarBrands()

        when(result) {
            is ApiResult.Success -> {
                _uiState.update { it.copy(allBrands = result.data, isFetchingBrands = false)}
            }
            is ApiResult.Error -> {
                _uiState.update { it.copy(errorMessage = result.message, isFetchingBrands = false) }
            }
        }
    }

    suspend fun fetchCarModels(brand: String) {
        _uiState.update { it.copy( isFetchingModels = true ) }
        val result = carModelRepository.getCarModelsForBrand(brand)

        when(result) {
            is ApiResult.Success -> {
                _uiState.update { it.copy(modelsForSelectedBrand = result.data, isFetchingModels = false)}
            }
            is ApiResult.Error -> {
                _uiState.update { it.copy(errorMessage = result.message, isFetchingModels = false) }
            }
        }
    }

    fun nextStep() {
        when (_uiState.value.currentStep) {
            ProfileStep.Personal -> {
                if (isPersonalInfoValid()) {
                    _uiState.update {
                        it.copy(
                            currentStep = ProfileStep.Car,
                            errorMessage = null
                        )
                    }
                    Log.d("NextStepButton", uiState.value.currentStep.toString())
                } else {
                    setError("Please fill in all required fields")
                }
            }

            ProfileStep.Car -> {
                // Do nothing - use completeProfileSetup() instead
            }
        }
    }

    fun previousStep() {
        when (_uiState.value.currentStep) {
            ProfileStep.Personal -> {
                // Do nothing - already on first step
            }

            ProfileStep.Car -> {
                _uiState.update {
                    it.copy(
                        currentStep = ProfileStep.Personal,
                        errorMessage = null
                    )
                }
            }
        }
    }


    fun completeProfileSetup(profileImageBytes: ByteArray?, carImageBytes: ByteArray?) {
        val fullName = _uiState.value.fullName
        val username = _uiState.value.username
        val nonNullBirthDate = _uiState.value.birthDate!!
        val country = _uiState.value.country
        val brand = _uiState.value.selectedBrand
        val model = _uiState.value.selectedModel

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }


                val carModelId = when (val result = carModelRepository.getCarModelId(brand, model)) {
                    is ApiResult.Success -> result.data
                    is ApiResult.Error -> {
                        setError(result.message)
                        return@launch
                    }
                }

                val createUserRequest = CreateUserRequest(
                    profilePicturePath = null,
                    fullName = fullName,
                    birthDate = nonNullBirthDate,
                    username = username,
                    country = country,
                )

                val createUserResult = userRepository.createUser(createUserRequest)

                val userId = when(createUserResult) {
                    is ApiResult.Success -> {
                        userPreferences.saveJwtToken(createUserResult.data.jwtToken)
                        userPreferences.saveUserId(createUserResult.data.userId)
                        createUserResult.data.userId
                    }
                    is ApiResult.Error -> {
                        setError(createUserResult.message)
                        return@launch
                    }
                }

                val userCarRequest = UserCarRequest(
                    userId = userId,
                    carModelId = carModelId,
                    imagePath = null,
                )

                val createUserCarResult = userCarRepository.createUserCar(userCarRequest)
                when(createUserCarResult) {
                    is ApiResult.Success -> {}
                    is ApiResult.Error -> {
                        setError(createUserCarResult.message)
                        return@launch
                    }
                }

                val profilePicturePath = profileImageBytes?.let {
                    val request = UploadImageRequest("profiles/$userId.jpg")
                    uploadImageAndGetPublicUrl(request, it)
                }

                val carPicturePath = carImageBytes?.let {
                    val request = UploadImageRequest("cars/$userId.jpg")
                    uploadImageAndGetPublicUrl(request, it)
                }

                val profilePictureRequest = UpdateProfilePictureRequest(profilePicturePath)
                val userCarUpdateRequest = UserCarUpdateRequest(carModelId, carPicturePath)

                val profileResponse = userRepository.updateProfilePicture(profilePictureRequest)
                val carResponse = userCarRepository.updateUserCar(userCarUpdateRequest)

                when(profileResponse) {
                    is ApiResult.Error -> setError(profileResponse.message)
                    is ApiResult.Success -> {}
                }

                when(carResponse) {
                    is ApiResult.Error -> setError(carResponse.message)
                    is ApiResult.Success -> {}
                }

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                setError(e.message.toString())
            }
        }
    }

    fun isPersonalInfoValid(): Boolean {
        val state = _uiState.value
        return state.fullName.isNotBlank() &&
                state.username.isNotBlank() &&
                state.country.isNotBlank() &&
                state.birthDate != null
    }

    private fun imageSourceToString(imageSource: ImageSource?): String? {
        return when (imageSource) {
            is ImageSource.Local -> imageSource.uri.toString()
            is ImageSource.Remote -> imageSource.url
            null -> null
        }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message, isLoading = false) }

        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun stringToImageSource(imageString: String?): ImageSource? {
        return when {
            imageString == null -> null
            imageString.startsWith("http") -> ImageSource.Remote(imageString)
            else -> ImageSource.Local(Uri.parse(imageString))
        }
    }

    private suspend fun uploadImageAndGetPublicUrl(request: UploadImageRequest, imageBytes: ByteArray): String {
        val uploadResponse = userRepository.getUploadUrl(request)

        return when (uploadResponse) {
            is ApiResult.Success -> {
                uploadToS3(uploadResponse.data.uploadUrl, imageBytes)
                uploadResponse.data.publicUrl
            }
            is ApiResult.Error -> throw IOException("Failed to get upload URL: ${uploadResponse.message}")
        }
    }
}

