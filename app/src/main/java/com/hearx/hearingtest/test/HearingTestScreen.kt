package com.hearx.hearingtest.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HearingTestScreen(navController: NavController) {

    val viewModel = hiltViewModel<HearingTestViewModel>()
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(state.round) {
        viewModel.startRound()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (state.uiState) {
            UiState.CountDown -> {
                HearingScreenCountDown(state.countDown)
            }

            UiState.RoundStarted -> HearingTestInputForm(
                round = state.round,
                onExitTestClick = {
                    navController.navigateUp()
                }, onSubmitClick = { input ->
                    viewModel.submitRound(input)
                }
            )

            UiState.SubmittingResults -> HearingTestSubmittingResults()
            UiState.SubmitResultsError -> HearingTestSubmitResultsError(
                onExitTestClick = {
                    navController.navigateUp()
                },
                onRetryClick = {
                    viewModel.retrySubmitResults()
                }
            )

            UiState.SubmitResultsSuccess -> HearingTestSubmitResultsSuccess(
                score = state.score,
                onDismissDialog = {
                    navController.navigateUp()
                }
            )
        }
    }
}

@Composable
fun HearingScreenCountDown(countDown: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("$countDown", fontSize = 32.sp)
    }
}

@Composable
fun HearingTestInputForm(round: Int, onExitTestClick: () -> Unit, onSubmitClick: (String) -> Unit) {

    var input by remember { mutableStateOf("") }
    val inputPattern = remember { Regex("^\\d+\$") }
    var isInputError by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text("Round $round of 10")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedTextField(
                input,
                label = { Text("Digits") },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { value ->
                    if (value == "" || (inputPattern.matches(value) && value.length <= 3)) {
                        input = value
                        isInputError = input.length < 3
                    }
                },
                isError = isInputError
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onExitTestClick
            ) {
                Text("Exit Test")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    focusManager.clearFocus()
                    onSubmitClick(input)
                },
                enabled = !isInputError
            ) {
                Text("Submit")
            }
        }
        Spacer(modifier = Modifier.imePadding())
    }
}


@Composable
fun HearingTestSubmittingResults() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Submitting results...", fontSize = 21.sp)
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun HearingTestSubmitResultsError(onExitTestClick: () -> Unit, onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text("Oops something went wrong.", fontSize = 21.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onExitTestClick
            ) {
                Text("Exit Test")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onRetryClick,
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun HearingTestSubmitResultsSuccess(score: Int, onDismissDialog: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(onDismissRequest = onDismissDialog) {
            Card(
                modifier = Modifier
                    .height(200.dp)
                    .padding(32.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Your score is: $score",
                        modifier = Modifier.padding(16.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = onDismissDialog,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            }
        }
    }
}
