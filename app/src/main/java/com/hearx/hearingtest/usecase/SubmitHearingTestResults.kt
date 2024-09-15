package com.hearx.hearingtest.usecase

import com.hearx.hearingtest.data.local.entity.HearingTestResult
import com.hearx.hearingtest.data.remote.SubmitHearingTestResultsRequest
import com.hearx.hearingtest.repository.HearingTestRepository
import com.hearx.hearingtest.test.TripletRound
import javax.inject.Inject

class SubmitHearingTestResults @Inject constructor(
    private val hearingTestRepository: HearingTestRepository
) {

    suspend operator fun invoke(score: Int, rounds: List<TripletRound>): SubmitResult {
        val rounds = rounds.map { it.toApiModel() }
        try {
            hearingTestRepository.submitResults(SubmitHearingTestResultsRequest(score, rounds))
        } catch (ex: Exception) {
            return SubmitResult.Error
        }

        hearingTestRepository.saveResult(HearingTestResult(score = score))

        return SubmitResult.Success
    }

}

sealed interface SubmitResult {
    data object Success : SubmitResult
    data object Error : SubmitResult
}

fun TripletRound.toApiModel() =
    com.hearx.hearingtest.data.remote.TripletRound(difficulty, tripletPlayed, tripletAnswer)
