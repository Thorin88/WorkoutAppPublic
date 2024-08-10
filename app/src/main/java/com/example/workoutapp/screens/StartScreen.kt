package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.navigation.NavSubGraphs
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.StartScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreenContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("StartScreenContent", "I got recomposed")

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->

        Column(
            modifier = modifier.padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text("Welcome!")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(NavSubGraphs.AuthSubGraph.route) {
                    // Want to ensure that we never end up with the situation where a back button
                    // press can result in the user skipping the logout action. Want logouts always
                    // being explicit.
                    popUpTo(NavSubGraphs.StartSubGraph.route) {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "To Login")
            }

        }

    }

}

@Composable
fun StartScreen(
    navController: NavController,
    startScreenViewModel: StartScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    StartScreenContent(
        navController = navController,
        snackbarHostState = startScreenViewModel.snackbarHostState,
        modifier = modifier,
    )

}

@Preview(showBackground = true)
@Composable
fun StartScreenContentPreview() {
    WorkoutAppTheme {
        StartScreenContent(
            navController = rememberNavController(),
            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}