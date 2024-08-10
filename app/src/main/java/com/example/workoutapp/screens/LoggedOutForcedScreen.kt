package com.example.workoutapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.ui.theme.WorkoutAppTheme

@Composable
fun ScreenWithApiUsage(
    navController: NavController,
    apiRequestState: WorkoutAPIRequestState,
    content: @Composable () -> Unit
) {
    // Check if the user is logged in based on API state
    if (apiRequestState is WorkoutAPIRequestState.Unauthenticated) {
        // If not logged in, display the forced logged-out screen
        LoggedOutForcedScreen(navController = navController)
    } else {
        // If logged in, display the content
        content()
    }
}

@Composable
fun LoggedOutForcedScreenContent(
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Your session has expired, please log in again")

        Button(
            onClick = {
                // Using the startDestination stored in the navController
                navController.navigate(navController.graph.startDestinationRoute.toString()) {
                    // Now clearing the entire app stack
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            Text(text = "Back To Login")
        }
    }

}

@Composable
fun LoggedOutForcedScreen(
    navController: NavController,
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    LoggedOutForcedScreenContent(
        navController = navController,
        modifier = modifier,
    )

}

@Preview(showBackground = true)
@Composable
fun LoggedOutForcedScreenContentPreview() {
    WorkoutAppTheme {
        LoggedOutForcedScreenContent(
            navController = rememberNavController(),
            modifier = Modifier.size(100.dp),
        )
    }
}