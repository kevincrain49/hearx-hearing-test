package com.hearx.hearingtest.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hearx.hearingtest.data.local.entity.HearingTestResult

@Composable
fun HearingTestResultsScreen(onExitClick: () -> Unit) {

    val viewModel = hiltViewModel<HearingTestResultsViewModel>()
    val viewState by viewModel.viewState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        when (viewState.uiState) {
            UiState.Loaded -> HearingTestResultsList(viewState.results, Modifier.weight(1f))
            UiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            UiState.NoResults -> {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No results found", fontSize = 21.sp)
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
            OutlinedButton(onClick = onExitClick) {
                Text("Exit")
            }
        }
    }

}

@Composable
fun HearingTestResultsList(results: List<HearingTestResult>, modifier: Modifier) {
    LazyColumn(modifier) {
        items(results) { result ->
            Text("Score: ${result.score}", fontSize = 21.sp, modifier = Modifier.padding(16.dp))
            HorizontalDivider()
        }
    }
}