package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.screens.helpers.GenericTopBar
import com.example.workoutapp.screens.helpers.RenderExpandedWorkout
import com.example.workoutapp.screens.helpers.RenderWorkoutComponent
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.TrackWorkoutsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackWorkoutComponentScreenContent(
    navController: NavController,

    workoutApiRequestState: WorkoutAPIRequestState,

    workouts : SavedWorkouts,
    selectedWorkoutIdx : Int?,

    finishWorkout : () -> Unit,

    workoutFinished : Boolean,

    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("TrackWorkoutComponentScreenContent", "I got recomposed")

    // No need for hoisting?
    // If its in this composable, then going back to this page before the VM lifecycle ends won't
    // result in seeing the same value?
    var selectedComponentIdx by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            GenericTopBar(
                navController = navController,
                title = "Track a Workout",
                includeBackArrow = true,
            )
        }
    ) { contentPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                // .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (workoutFinished) {
                Text(text = "Workout Complete!")
                return@Scaffold
            }

            if (selectedWorkoutIdx == null) {
                return@Scaffold
            }

            if (workoutApiRequestState is WorkoutAPIRequestState.Loading) {
                // TODO
                CircularProgressIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
                return@Scaffold
            }

            RenderWorkoutComponent(
                workoutComponent = workouts[selectedWorkoutIdx][selectedComponentIdx],
                workoutIdx = selectedWorkoutIdx,
                componentIdx = selectedComponentIdx,
                selected = true,
                onClick = {},
                editMode = false,
                updateWorkoutComponent = { _, _, _ -> },
                hasWorkoutComponentChanged = { _, _ -> false },
            )

            Spacer(modifier = Modifier.height(32.dp))

            val show_prev_button = (selectedComponentIdx > 0)
            val show_next_button = (selectedComponentIdx < workouts[selectedWorkoutIdx].size - 1)

            Row(
                modifier = Modifier
                    // .fillMaxSize()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                if (show_prev_button) {
                    Button(onClick = {
                        selectedComponentIdx--
                    }) {
                        Text(text = "Previous Exercise")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (show_next_button) {
                    Button(onClick = {
                        selectedComponentIdx++
                    }) {
                        Text(text = "Next Exercise")
                    }
                }
                else {
                    Button(onClick = {
                        finishWorkout()
                        // TODO -> Signal to UI that the workout is now finished
                    }) {
                        Text(text = "Finish Exercise")
                    }
                }

            }

            // Row(verticalAlignment = Alignment.CenterVertically) {
            //     if (showPrevButton) {
            //         IconButton(onClick = { onPrevClick() }) {
            //             Icon(Icons.Default.ArrowBack, contentDescription = "Previous Exercise")
            //         }
            //     } else {
            //         Spacer(modifier = Modifier.size(48.dp)) // Size of the IconButton
            //     }
            //
            //     Spacer(modifier = Modifier.width(8.dp))
            //
            //     IconButton(onClick = { onNextClick() }) {
            //         if (selectedComponentIdx == workouts[selectedWorkoutIdx].size - 1) {
            //             Text(text = "Finish")
            //         } else {
            //             Icon(Icons.Default.ArrowForward, contentDescription = "Next Exercise")
            //         }
            //     }
            // }

            Spacer(modifier = Modifier.height(32.dp))

        }

    }

}

@Composable
fun TrackWorkoutComponentScreen(
    navController: NavController,
    trackWorkoutsScreenViewModel: TrackWorkoutsScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    ScreenWithApiUsage(
        navController = navController,
        apiRequestState = trackWorkoutsScreenViewModel.workoutApiRequestState,
        content = {
            TrackWorkoutComponentScreenContent(
                navController = navController,

                workoutApiRequestState = trackWorkoutsScreenViewModel.workoutApiRequestState,

                workouts = trackWorkoutsScreenViewModel.workouts,
                selectedWorkoutIdx = trackWorkoutsScreenViewModel.selectedWorkoutIdx,

                finishWorkout = { trackWorkoutsScreenViewModel.attemptToFinishWorkout() },

                workoutFinished = trackWorkoutsScreenViewModel.workoutFinished,

                snackbarHostState = trackWorkoutsScreenViewModel.snackbarHostState,
                modifier = modifier,
            )
        },
    )

}

@Preview(showBackground = true)
@Composable
fun TrackWorkoutComponentScreenContentPreview() {
    WorkoutAppTheme {
        TrackWorkoutComponentScreenContent(
            navController = rememberNavController(),

            workoutApiRequestState = WorkoutAPIRequestState.Ready,

            workouts = SavedWorkouts(workouts = listOf()),
            selectedWorkoutIdx = null,

            finishWorkout = {  },

            workoutFinished = false,

            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}