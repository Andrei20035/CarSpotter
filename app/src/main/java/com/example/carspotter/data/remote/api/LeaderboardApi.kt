package com.example.carspotter.data.remote.api

import com.example.carspotter.data.remote.dto.leaderboard.LeaderboardResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("limit") limit: Int = 50,
    ): Response<LeaderboardResponseDto>
}
