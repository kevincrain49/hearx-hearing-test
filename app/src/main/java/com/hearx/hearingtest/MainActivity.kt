package com.hearx.hearingtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hearx.hearingtest.results.HearingTestResultsScreen
import com.hearx.hearingtest.test.HearingTestScreen
import com.hearx.hearingtest.ui.theme.HeartingTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()
        setContent {
            HeartingTestTheme(dynamicColor = false, darkTheme = true) {
                Scaffold { contentPadding ->
                    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(contentPadding)) {
                        HomeRouter()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeRouter() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onStartTestClick = {
                    navController.navigate(HearingTest)
                },
                onViewResultsClick = {
                    navController.navigate(ViewHearingTestResults)
                }
            )
        }
        composable<HearingTest> {
            HearingTestScreen(navController)
        }
        composable<ViewHearingTestResults> {
            HearingTestResultsScreen(onExitClick = {
                navController.navigateUp()
            })
        }
    }
}

@Composable
fun HomeScreen(onStartTestClick: () -> Unit, onViewResultsClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_hearx_light),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(64.dp))
            OutlinedButton(onClick = onStartTestClick) {
                Text("Start Test")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onViewResultsClick) {
                Text("View Results")
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HeartingTestTheme {
        HomeScreen(onStartTestClick = {}, onViewResultsClick = {})
    }
}