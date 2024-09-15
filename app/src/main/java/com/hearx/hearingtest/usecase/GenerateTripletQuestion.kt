package com.hearx.hearingtest.usecase

import javax.inject.Inject

class GenerateTripletQuestion @Inject constructor() {

    operator fun invoke(
        tripletQuestions: Set<String>,
        previousTripletQuestion: String?
    ): String {
        var tripletQuestion: String? = null
        while (tripletQuestion == null) {
            val first = (1..9).random()
            val second = (1..9).random()
            val third = (1..9).random()
            if (previousTripletQuestion != null) {
                if (previousTripletQuestion[0].digitToInt() == first) continue
                if (previousTripletQuestion[1].digitToInt() == second) continue
                if (previousTripletQuestion[2].digitToInt() == third) continue
            }
            val currentTripletQuestion = "$first$second$third"
            if (currentTripletQuestion in tripletQuestions) continue
            tripletQuestion = currentTripletQuestion
        }

        return tripletQuestion
    }

}