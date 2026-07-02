package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import com.example.carspotter.data.remote.api.ActivityApi
import com.example.carspotter.data.remote.dto.activity.toDomain
import com.example.carspotter.features.activity.model.ActivityData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityApi: ActivityApi,
) : ActivityRepository {

    override suspend fun getActivity(limit: Int, timezone: String?): ApiResult<ActivityData> {
        return when (val result = safeApiCall { activityApi.getActivity(limit, timezone) }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
