package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import com.example.carspotter.data.remote.api.LeaderboardApi
import com.example.carspotter.data.remote.dto.leaderboard.toDomain
import com.example.carspotter.features.leaderboard.LeaderboardResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderboardRepositoryImpl @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
) : LeaderboardRepository {

    override suspend fun getLeaderboard(): ApiResult<LeaderboardResult> {
        return when (val result = safeApiCall { leaderboardApi.getLeaderboard() }) {
            is ApiResult.Success -> ApiResult.Success(result.data.toDomain())
            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }
}
