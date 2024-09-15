package com.hearx.hearingtest.test

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hearx.hearingtest.audio.HearingTestAudioPlayer
import com.hearx.hearingtest.usecase.GenerateTripletQuestion
import com.hearx.hearingtest.usecase.SubmitHearingTestResults
import com.hearx.hearingtest.usecase.SubmitResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HearingTestViewModel @Inject constructor(
    private val generateTripletQuestion: GenerateTripletQuestion,
    private val hearingTestAudioPlayer: HearingTestAudioPlayer,
    private val submitHearingTestResults: SubmitHearingTestResults
) : ViewModel() {

    private val _viewState = MutableStateFlow(HearingTestViewState())
    val viewState = _viewState.asStateFlow()

    private val difficulty = mutableIntStateOf(5)
    private val tripletQuestion = mutableStateOf<String?>(null)
    private val previousTripletQuestion = mutableStateOf<String?>(null)
    private val tripletQuestions = mutableSetOf<String>()
    private val score = mutableIntStateOf(0)
    private val tripletRounds = mutableListOf<TripletRound>()

    suspend fun startRound() {
        startCountDown()

        val currentState = viewState.value
        _viewState.emit(currentState.copy(uiState = UiState.RoundStarted))

        val triplet = generateTripletQuestion(tripletQuestions, previousTripletQuestion.value)
        tripletQuestion.value = triplet

        hearingTestAudioPlayer.play(difficulty.intValue, triplet)
    }

    private suspend fun startCountDown() {
        val currentState = _viewState.value
        val round = currentState.round
        var countDown = if(round == 1) 3 else 2

        _viewState.emit(currentState.copy(uiState = UiState.CountDown, countDown = countDown))
        repeat(countDown) {
            _viewState.emit(currentState.copy(countDown = countDown--))
            delay(1000L)
        }
    }

    fun submitRound(tripletAnswer: String) {
        hearingTestAudioPlayer.stopPlaying()

        previousTripletQuestion.value = tripletQuestion.value
        val currentDifficulty = difficulty.intValue
        if (tripletAnswer == tripletQuestion.value) {
            score.intValue += currentDifficulty
            difficulty.intValue = (currentDifficulty + 1).coerceAtMost(10)
        } else {
            difficulty.intValue = (currentDifficulty - 1).coerceAtLeast(1)
        }
        tripletRounds += TripletRound(currentDifficulty, tripletQuestion.value!!, tripletAnswer)

        viewModelScope.launch {
            val currentState = _viewState.value
            val nextRound = currentState.round + 1
            if (nextRound > 10) {
                submitResults()
            } else {
                _viewState.emit(currentState.copy(round = currentState.round + 1, uiState = UiState.CountDown))
            }
        }
    }

    fun retrySubmitResults() {
        viewModelScope.launch {
            submitResults()
        }
    }

    private suspend fun submitResults() {
        val currentState = _viewState.value
        _viewState.emit(currentState.copy(uiState = UiState.SubmittingResults))

        val result = submitHearingTestResults(score.intValue, tripletRounds)
        when(result) {
            SubmitResult.Error -> {
                _viewState.emit(currentState.copy(uiState = UiState.SubmitResultsError))
            }
            SubmitResult.Success -> {
                _viewState.emit(currentState.copy(uiState = UiState.SubmitResultsSuccess, score = score.intValue))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        hearingTestAudioPlayer.stopPlaying()
    }

}

data class HearingTestViewState(
    val uiState: UiState = UiState.CountDown,
    val round: Int = 1,
    val countDown: Int = 3,
    val score: Int = 0
)

sealed interface UiState {
    data object CountDown : UiState
    data object RoundStarted : UiState
    data object SubmittingResults : UiState
    data object SubmitResultsSuccess : UiState
    data object SubmitResultsError : UiState
}

data class TripletRound(
    val difficulty: Int,
    val tripletPlayed: String,
    val tripletAnswer: String
)