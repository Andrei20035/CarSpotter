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
import com.example.carspotter.data.repository.ImageRepository
import com.example.carspotter.data.repository.UserCarRepository
import com.example.carspotter.data.repository.UserRepository
import com.example.carspotter.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileCustomizationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userCarRepository: UserCarRepository,
    private val userPreferences: UserPreferences,
    private val carModelRepository: CarModelRepository,
    private val imageRepository: ImageRepository,
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
        _uiState.update { it.copy(carPicture = imageSource) }
    }

    fun updateCarBrand(brand: String) {
        _uiState.update { it.copy(selectedBrand = brand) }

        viewModelScope.launch {
            loadModelsForBrand()
        }
    }

    fun updateCarModel(model: String) {
        _uiState.update { it.copy(selectedModel = model) }
    }

    suspend fun loadCarBrands() {
        _uiState.update { it.copy(isFetchingBrands = true) }
        val brands = carModelRepository.getAllCarBrands()

        when (brands) {
            is ApiResult.Success -> {
                _uiState.update { it.copy(allBrands = brands.data) }
                Log.d("CAR BRANDS", "Car brands successfully loaded")
                Log.d("CAR BRANDS", _uiState.value.allBrands.toString())
            }

            is ApiResult.Error -> {
                setError(brands.message)
                Log.d("CAR BRANDS", "Error when loading car brands")
            }
        }
        _uiState.update { it.copy(isFetchingBrands = false) }

    }

    suspend fun loadModelsForBrand() {
        _uiState.update { it.copy(isFetchingModels = true) }
        val models = carModelRepository.getModelsForBrand(_uiState.value.selectedBrand)

        when (models) {
            is ApiResult.Success -> {
                _uiState.update { it.copy(modelsForSelectedBrand = models.data) }
            }

            is ApiResult.Error -> setError(models.message)
        }
        _uiState.update { it.copy(isFetchingModels = false) }

    }

    fun nextStep() {
        when (_uiState.value.currentStep) {
            ProfileStep.Personal -> {
                if (isPersonalInfoValid()) {
                    viewModelScope.launch {
                        loadCarBrands()
                        _uiState.update {
                            it.copy(
                                currentStep = ProfileStep.Car,
                                errorMessage = null
                            )
                        }
                        Log.d("NextStepButton", _uiState.value.currentStep.toString())
                    }
                } else {
                    setError("Please fill in all required fields")
                }
            }

            ProfileStep.Car -> {

            }
        }
    }

    fun previousStep() {
        when (_uiState.value.currentStep) {
            ProfileStep.Personal -> {
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



    fun completeProfileSetup(
        profileImageBytes: ByteArray?,
        profileImageMime: String?,
        carImageBytes: ByteArray?,
        carImageMime: String?
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val userId = createUserProfile()

                val nonNullCarImageMime = carImageMime!!
                val nonNullProfileImageMime = profileImageMime!!

                if(userId != null) {
                    val carModelId = createUserCarIfNeeded(userId)
                    uploadImagesIfNeeded(
                        carModelId = carModelId,
                        userId = userId,
                        carImageBytes = carImageBytes,
                        carImageMime = nonNullCarImageMime,
                        profileImageBytes = profileImageBytes,
                        profileImageMime = nonNullProfileImageMime
                    )
                }
                _uiState.update { it.copy(isLoading = false, isUserCreated = true) }
            } catch (e: Exception) {
                setError(e.message.toString())
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun createUserProfile(): UUID? {
        val createUserRequest = CreateUserRequest(
            fullName = _uiState.value.fullName,
            birthDate = _uiState.value.birthDate!!,
            username = _uiState.value.username,
            country = _uiState.value.country,
        )
        Log.d("BIRTHDATE" ,_uiState.value.birthDate.toString())
        return when (val result = userRepository.createUser(createUserRequest)) {
            is ApiResult.Success -> {
                userPreferences.saveJwtToken(result.data.jwtToken)
                userPreferences.saveUserId(result.data.userId)
                result.data.userId
            }
            is ApiResult.Error -> {
                setError(result.message)
                Log.d("ERROR", "Error in user profile creation")
                null
            }
        }
    }

    private suspend fun createUserCarIfNeeded(userId: UUID): UUID? {
        val brand = _uiState.value.selectedBrand
        val model = _uiState.value.selectedModel

        if(brand.isEmpty() || model.isEmpty()) {
            return null
        }

        val carModelId = when(val result = carModelRepository.getCarModelId(brand, model)) {
            is ApiResult.Success -> result.data.id
            is ApiResult.Error -> {
                setError(result.message)
                null
            }
        }

        if(carModelId == null) {
            return null
        }

        val userCarRequest = UserCarRequest(
            userId = userId,
            carModelId = carModelId,
            imagePath = null,
        )
        return when (val result = userCarRepository.createUserCar(userCarRequest)) {
            is ApiResult.Success -> carModelId
            is ApiResult.Error -> {
                setError(result.message)
                null
            }
        }
    }

    private suspend fun uploadImagesIfNeeded(
        carModelId: UUID?,
        userId: UUID,
        profileImageBytes: ByteArray?,
        profileImageMime: String,
        carImageBytes: ByteArray?,
        carImageMime: String
    ) {
        var profileResponse: ApiResult<Unit>? = null
        var carResponse: ApiResult<Unit>? = null

        profileImageBytes?.let {
            val request = UploadImageRequest("profiles/$userId.jpg")
            val profilePicturePath = imageRepository.uploadImageAndGetPublicUrl(
                request = request,
                imageBytes = it,
                mimeType = profileImageMime
            )

            val profilePictureRequest = UpdateProfilePictureRequest(profilePicturePath)
            profileResponse = userRepository.updateProfilePicture(profilePictureRequest)
        }

        carImageBytes?.let {
            val request = UploadImageRequest("cars/$userId.jpg")
            val carPicturePath = imageRepository.uploadImageAndGetPublicUrl(
                request = request,
                imageBytes = it,
                mimeType = carImageMime
            )
            carModelId?.let{
                val userCarUpdateRequest = UserCarUpdateRequest(carModelId, carPicturePath)
                carResponse = userCarRepository.updateUserCar(userCarUpdateRequest)
            }
        }


        profileResponse?.let {
            when (it) {
                is ApiResult.Error -> setError(it.message)
                is ApiResult.Success -> {}
            }
        }

        carResponse?.let {
            when (it) {
                is ApiResult.Error -> setError(it.message)
                is ApiResult.Success -> {}
            }
        }
    }

    fun isPersonalInfoValid(): Boolean {
        val state = _uiState.value
        return state.fullName.isNotBlank() &&
                state.username.isNotBlank() &&
                state.birthDate != null
    }


    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message, isLoading = false) }

        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(errorMessage = null) }
        }
    }

}

