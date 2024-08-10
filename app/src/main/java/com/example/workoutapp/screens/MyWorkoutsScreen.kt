package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.navigation.Screen
import com.example.workoutapp.screens.helpers.GenericTopBar
import com.example.workoutapp.screens.helpers.RenderWorkoutCard
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.MyWorkoutsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyWorkoutsScreenContent(
    navController: NavController,

    workoutApiRequestState: WorkoutAPIRequestState,

    workouts : SavedWorkouts,
    updateWorkouts : () -> Unit,

    updateSelectedWorkoutIdx : (Int) -> Unit,


    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("MyWorkoutsScreenContent", "I got recomposed")

    // Runs once per composable's lifetime
    LaunchedEffect(Unit) {

        updateWorkouts()

    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            GenericTopBar(
                navController = navController,
                title = "My Workouts",
                includeBackArrow = true,
            )
        }
    ) { contentPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                // .verticalScroll(rememberScrollState()) // May need this, cannot remember exactly how lazy columns work
                // TODO -> May actually want to restrict the size of the scrollable lazy column.
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (workoutApiRequestState is WorkoutAPIRequestState.Loading) {

                CircularProgressIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )

                return@Scaffold

            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(workouts.workouts.size) { idx ->
                    RenderWorkoutCard(
                        workout = workouts.workouts[idx],
                        onClick = {
                            // TODO -> set selected or go to a new screen
                            updateSelectedWorkoutIdx(idx)
                            // Currently ok, but assumes that we don't want to return to this screen
                            // with the intent of seeing new workouts before the screen has
                            // left the stack. i.e. needs to change if you can add workouts and
                            // then return backwards to this screen.
                            navController.navigate(Screen.WorkoutScreen.route) {
                                // navController.popBackStack()
                            }
                        },
                    )
                }
            }

            // Text(text = workoutApiRequestState.toString())

        }

    }

}

@Composable
fun MyWorkoutsScreen(
    navController: NavController,
    myWorkoutScreenViewModel: MyWorkoutsScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    ScreenWithApiUsage(
        navController = navController,
        apiRequestState = myWorkoutScreenViewModel.workoutApiRequestState,
        content = {
            MyWorkoutsScreenContent(
                navController = navController,

                workoutApiRequestState = myWorkoutScreenViewModel.workoutApiRequestState,

                workouts = myWorkoutScreenViewModel.workouts,
                updateWorkouts = { myWorkoutScreenViewModel.updateUserWorkouts() },

                updateSelectedWorkoutIdx = { idx -> myWorkoutScreenViewModel.updateSelectedIdx(idx) },

                snackbarHostState = myWorkoutScreenViewModel.snackbarHostState,
                modifier = modifier,
            )
        },
    )

}

@Preview(showBackground = true)
@Composable
fun MyWorkoutsContentPreview() {
    WorkoutAppTheme {
        MyWorkoutsScreenContent(
            navController = rememberNavController(),

            workoutApiRequestState = WorkoutAPIRequestState.Ready,

            workouts = SavedWorkouts(workouts = listOf()),
            updateWorkouts = {},

            updateSelectedWorkoutIdx = {},

            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}