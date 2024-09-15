package com.hearx.hearingtest.repository

import com.hearx.hearingtest.data.local.HearingTestDatabase
import com.hearx.hearingtest.data.local.entity.HearingTestResult
import com.hearx.hearingtest.data.remote.HearingTestApi
import com.hearx.hearingtest.data.remote.SubmitHearingTestResultsRequest
import com.hearx.hearingtest.data.remote.SubmitHearingTestResultsResponse
import javax.inject.Inject

class HearingTestRepository @Inject constructor(
    private val hearingTestApi: HearingTestApi,
    private val hearingTestDatabase: HearingTestDatabase
) {

    suspend fun submitResults(request: SubmitHearingTestResultsRequest): SubmitHearingTestResultsResponse {
        return hearingTestApi.submitResults(request)
    }

    suspend fun saveResult(result: HearingTestResult) {
        hearingTestDatabase.hearingTestResultDao().insert(result)
    }

    suspend fun getResults(): List<HearingTestResult> {
        return hearingTestDatabase.hearingTestResultDao().getAll()
    }

}