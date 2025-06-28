package com.example.carspotter.ui.screens.profile_customization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carspotter.data.local.preferences.UserPreferences
import com.example.carspotter.data.remote.model.user.CreateUserRequest
import com.example.carspotter.data.remote.model.user_car.UserCarRequest
import com.example.carspotter.data.repository.CarModelRepository
import com.example.carspotter.data.repository.UserCarRepository
import com.example.carspotter.data.repository.UserRepository
import com.example.carspotter.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
        _uiState.update { it.copy(profileImage = imageSource) }
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
            ApiResult.Loading -> {}

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
            ApiResult.Loading -> {}

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


    fun completeProfileSetup() {
        viewModelScope.launch {
            try {
                val nonNullBirthDate = _uiState.value.birthDate!!
                val profilePicturePath = _uiState.value.profileImage
                val carImage = _uiState.value.carImage
                val fullName = _uiState.value.fullName
                val username = _uiState.value.username
                val country = _uiState.value.country
                val brand = _uiState.value.selectedBrand
                val model = _uiState.value.selectedModel
                var carModelId: Int? = null

                _uiState.update { it.copy(isLoading = true) }

                val createUserRequest = CreateUserRequest(
                    profilePicturePath = imageSourceToString(profilePicturePath),
                    fullName = fullName,
                    birthDate = nonNullBirthDate,
                    username = username,
                    country = country,
                )

                val createUserResult = userRepository.createUser(createUserRequest)

                when(createUserResult) {
                    is ApiResult.Success -> {
                        userPreferences.saveJwtToken(createUserResult.data.jwtToken)
                        userPreferences.saveUserId(createUserResult.data.userId)
                    }
                    is ApiResult.Error -> {
                        setError(createUserResult.message)
                    }
                    is ApiResult.Loading -> {}
                }

                val userId = userPreferences.userId.firstOrNull() ?: throw IllegalStateException("User ID missing")
                val getCarModelIdResult = carModelRepository.getCarModelId(brand, model)

                when(getCarModelIdResult) {
                    is ApiResult.Success -> {
                        carModelId = getCarModelIdResult.data
                    }
                    is ApiResult.Error -> {
                        setError(getCarModelIdResult.message)
                    }
                    ApiResult.Loading -> {}
                }

                val userCarRequest = UserCarRequest(
                    userId = userId,
                    carModelId = carModelId ?: throw IllegalStateException("Car Model ID missing"),
                    imagePath = imageSourceToString(carImage),
                )

                val createUserCarResult = userCarRepository.createUserCar(userCarRequest)

                when(createUserCarResult) {
                    is ApiResult.Success -> {

                    }
                    is ApiResult.Error -> {
                       setError(createUserCarResult.message)
                    }
                    ApiResult.Loading -> {}
                }

                _uiState.update { it.copy(isLoading = false) }



            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "An error occurred"
                    )
                }
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
}