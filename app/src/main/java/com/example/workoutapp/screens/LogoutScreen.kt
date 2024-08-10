package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.navigation.NavSubGraphs
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.LogoutScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutScreenContent(
    navController: NavController,

    workoutApiRequestState: WorkoutAPIRequestState,

    logout: suspend () -> Unit, // Important to annotate suspend here, to avoid misuse
    onSuccessfulLogout: () -> Unit,
    navigateAway: Boolean,

    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("LogoutScreenContent", "I got recomposed")

    val logoutRequestSuccessful = (workoutApiRequestState !is WorkoutAPIRequestState.Success)

    // So to request a logout, this state needs to be Loading
    if (workoutApiRequestState is WorkoutAPIRequestState.Loading) {

        // Will get called if this conditional branch is removed and re-added to the composition tree.
        // For example, this block runs. The state changes, and this block doesn't run. The coroutine is
        // cancelled (or already completed). If the block should run again after another state change, it will
        // launch the effect again. But what this effort and branch does do is that recompositions with the
        // same state value will not cause re-runs of this routine.

        LaunchedEffect(workoutApiRequestState) {
            logout()
        }

    }

    if (navigateAway) {

        Log.d("LogoutScreenContent", "Successful Logout, attempting to navigate to next screen")

        // Changes navigateAway to back to false, to avoid navigation rerunning this section
        // as navigate() will recompose the current composable.
        onSuccessfulLogout()

        navController.navigate(NavSubGraphs.StartSubGraph.route) {
            popUpTo(NavSubGraphs.LogoutSubGraph.route) {
                inclusive = true
            }
        }

        // No return here, instead the state initiating the navigation is equivalent to the state that
        // was before the state change, in terms of how the UI looks. This prevents a jumpy UI.

    }

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

            Text(
                text = "Logging you out...",
            )

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.surfaceVariant,
            )

        }

    }

}

@Composable
fun LogoutScreen(
    navController: NavController,
    logoutScreenViewModel: LogoutScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    // TODO -> See if there's better practice for avoiding having 1 million things passed here (chatgpt may know)
    LogoutScreenContent(
        navController = navController,

        workoutApiRequestState = logoutScreenViewModel.workoutApiRequestState,

        logout = { logoutScreenViewModel.attemptLogout() },
        onSuccessfulLogout = { logoutScreenViewModel.onSuccessfulLogout() },
        navigateAway = logoutScreenViewModel.navigateAway,

        snackbarHostState = logoutScreenViewModel.snackbarHostState,
        modifier = modifier,
    )

}

@Preview(showBackground = true)
@Composable
fun LogoutScreenContentPreview() {
    WorkoutAppTheme {
        LogoutScreenContent(
            navController = rememberNavController(),

            workoutApiRequestState = WorkoutAPIRequestState.Ready,
            logout = {},
            onSuccessfulLogout = {},
            navigateAway = false,

            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}