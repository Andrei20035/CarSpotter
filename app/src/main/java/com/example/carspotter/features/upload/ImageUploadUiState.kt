package com.example.carspotter.features.upload

import android.net.Uri
import com.example.carspotter.data.remote.dto.car_model.CarModelOption

/** Subtle, non-blocking state of the optional location capture. */
enum class LocationStatus { Idle, Resolving, Added, Unavailable }

/**
 * UI state for the Upload Photo screen. Immutable; mutated only by [ImageUploadViewModel].
 */
data class ImageUploadUiState(
    val imageUri: Uri? = null,

    // Optional location, attached best-effort. All nullable; posting never depends on these.
    val latitude: Double? = null,
    val longitude: Double? = null,
    val town: String? = null,
    val country: String? = null,
    val locationStatus: LocationStatus = LocationStatus.Idle,

    // Brand dropdown.
    val brands: List<String> = emptyList(),
    val isLoadingBrands: Boolean = false,
    val brandsError: String? = null,
    val selectedBrand: String? = null,
    val brandDropdownOpen: Boolean = false,

    // Model dropdown (populated after a brand is chosen).
    val models: List<CarModelOption> = emptyList(),
    val isLoadingModels: Boolean = false,
    val modelsError: String? = null,
    val selectedModel: CarModelOption? = null,
    val modelDropdownOpen: Boolean = false,

    val description: String = "",

    val isPosting: Boolean = false,
    val postSuccess: Boolean = false,

    // One-shot message for transient errors (brand/model fetch, post failure).
    val userMessage: String? = null,
) {
    /** The model dropdown is interactive only once a brand is selected. */
    val isModelDropdownEnabled: Boolean
        get() = selectedBrand != null

    /** Post requires an image, a brand, and a model, and no submission in flight. */
    val canPost: Boolean
        get() = imageUri != null && selectedBrand != null && selectedModel != null && !isPosting

    /** Display names for the model dropdown list. */
    val modelNames: List<String>
        get() = models.map { it.model }
}
