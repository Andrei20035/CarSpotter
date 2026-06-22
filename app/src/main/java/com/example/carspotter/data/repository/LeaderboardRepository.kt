package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.features.leaderboard.LeaderboardResult

interface LeaderboardRepository {
    suspend fun getLeaderboard(): ApiResult<LeaderboardResult>
}
