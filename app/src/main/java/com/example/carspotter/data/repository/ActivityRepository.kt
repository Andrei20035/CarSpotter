package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.features.activity.model.ActivityData

interface ActivityRepository {
    suspend fun getActivity(limit: Int = 50, timezone: String? = null): ApiResult<ActivityData>
}
