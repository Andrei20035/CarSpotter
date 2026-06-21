package com.example.carspotter.data.repository

import com.example.carspotter.core.network.ApiResult
import com.example.carspotter.core.network.safeApiCall
import com.example.carspotter.data.model.ReportReason
import com.example.carspotter.data.remote.api.ReportApi
import com.example.carspotter.data.remote.dto.report.ReportRequest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface ReportRepository {
    suspend fun reportPost(postId: UUID, reason: ReportReason): ApiResult<Unit>
}

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi,
) : ReportRepository {

    override suspend fun reportPost(postId: UUID, reason: ReportReason): ApiResult<Unit> {
        return safeApiCall { reportApi.reportPost(postId, ReportRequest(reason)) }
    }
}
