package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
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
import com.example.workoutapp.screens.helpers.makeNamePretty
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.MyWorkoutsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreenContent(
    navController: NavController,

    workoutApiRequestState: WorkoutAPIRequestState,

    workouts : SavedWorkouts,
    selectedWorkoutIdx : Int?,

    editMode : Boolean,
    switchEditMode : () -> Unit,

    updateWorkoutComponent: (Int, Int, WorkoutComponent) -> Unit,
    hasWorkoutComponentChanged: (Int, Int) -> Boolean,

    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("WorkoutScreenContent", "I got recomposed")

    // No need for hoisting?
    // If its in this composable, then going back to this page before the VM lifecycle ends won't
    // result in seeing the same value?
    var selectedComponentIdx by rememberSaveable { mutableIntStateOf(-1) }

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
                firstAction = {
                    // Just needs to show different icons. The VM will handle the heavier
                    // validation/saving calls when the state value switches to false.

                    // VM needs to do things when switch is called.
                    IconButton(
                        onClick = {
                            switchEditMode()
                        }
                    ) {
                        if (!editMode) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Components"
                            )
                        }
                        else {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Save additions"
                            )
                        }
                    }
                }
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = makeNamePretty(workouts[selectedWorkoutIdx].name))

            Spacer(modifier = Modifier.height(16.dp))

            RenderExpandedWorkout(
                workout = workouts[selectedWorkoutIdx],
                workoutIdx = selectedWorkoutIdx,
                componentOnClick = { idx: Int -> selectedComponentIdx = idx },
                selectedComponentIdx = selectedComponentIdx,
                modifier = Modifier.weight(0.8f),
                editMode = editMode,
                updateWorkoutComponent = updateWorkoutComponent,
                hasWorkoutComponentChanged = hasWorkoutComponentChanged,
            )

            Spacer(modifier = Modifier.height(8.dp))

            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Component"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

        }

    }

}

@Composable
fun WorkoutScreen(
    navController: NavController,
    myWorkoutsScreenViewModel: MyWorkoutsScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    ScreenWithApiUsage(
        navController = navController,
        apiRequestState = myWorkoutsScreenViewModel.workoutApiRequestState,
        content = {
            WorkoutScreenContent(
                navController = navController,

                workoutApiRequestState = myWorkoutsScreenViewModel.workoutApiRequestState,

                workouts = myWorkoutsScreenViewModel.workouts,
                selectedWorkoutIdx = myWorkoutsScreenViewModel.selectedWorkoutIdx,

                editMode = myWorkoutsScreenViewModel.editMode,
                switchEditMode = { myWorkoutsScreenViewModel.switchEditMode() },

                updateWorkoutComponent = { wIdx, wcIdx, wc ->
                    myWorkoutsScreenViewModel.updateComponent(
                        workoutIdx = wIdx,
                        componentIdx = wcIdx,
                        newWorkoutComponent = wc,
                    )
                 },
                hasWorkoutComponentChanged = { wIdx, wcIdx ->
                    myWorkoutsScreenViewModel.hasChangedWorkoutComponentChanged(
                        workoutIdx = wIdx,
                        componentIdx = wcIdx,
                    )
                },

                snackbarHostState = myWorkoutsScreenViewModel.snackbarHostState,
                modifier = modifier,
            )
        },
    )

}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenContentPreview() {
    WorkoutAppTheme {
        WorkoutScreenContent(
            navController = rememberNavController(),

            workoutApiRequestState = WorkoutAPIRequestState.Ready,

            workouts = SavedWorkouts(workouts = listOf()),
            selectedWorkoutIdx = null,

            editMode = false,
            switchEditMode = {},

            updateWorkoutComponent = { _, _, _ -> },
            hasWorkoutComponentChanged = { _, _, -> false },

            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}