package com.hearx.hearingtest.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

interface HearingTestApi {

    @POST("/")
    suspend fun submitResults(@Body request: SubmitHearingTestResultsRequest): SubmitHearingTestResultsResponse

}

data class SubmitHearingTestResultsRequest(
    val score: Int,
    val rounds: List<TripletRound>
)

class SubmitHearingTestResultsResponse

data class TripletRound(
    val difficulty: Int,
    @SerializedName("triplet_played") val tripletPlayed : String,
    @SerializedName("triplet_answered") val tripletAnswered : String
)

