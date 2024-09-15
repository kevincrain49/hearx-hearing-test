package com.hearx.hearingtest.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hearx.hearingtest.data.local.entity.HearingTestResult
import com.hearx.hearingtest.repository.HearingTestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HearingTestResultsViewModel @Inject constructor(
    private val hearingTestRepository: HearingTestRepository
): ViewModel() {

    private val _viewState = MutableStateFlow(HearingTestResults())
    val viewState = _viewState.asStateFlow()

    init {
        loadHearingTestResults()
    }

    private fun loadHearingTestResults() {
        viewModelScope.launch {
            delay(1000L)
            _viewState.emit(viewState.value.copy(uiState = UiState.Loading))
            val results = hearingTestRepository.getResults()
            val uiState = if(results.isEmpty()) UiState.NoResults else UiState.Loaded
            _viewState.emit(viewState.value.copy(results = results, uiState = uiState))
        }
    }

}

data class HearingTestResults(
    val uiState: UiState = UiState.Loading,
    val results: List<HearingTestResult> = emptyList()
)

sealed interface UiState {
    data object Loading: UiState
    data object NoResults: UiState
    data object Loaded: UiState
}