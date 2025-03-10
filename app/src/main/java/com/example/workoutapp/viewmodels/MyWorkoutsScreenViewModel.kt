package com.example.workoutapp.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.workouts.GetUserWorkoutsResult
import com.example.workoutapp.domain.workouts.GetUserWorkoutsUseCase
import com.example.workoutapp.domain.workouts.UpdateWorkoutComponentsResult
import com.example.workoutapp.domain.workouts.UpdateWorkoutComponentsUseCase
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWorkoutsScreenViewModel @Inject constructor (
    val getUserWorkoutsUseCase: GetUserWorkoutsUseCase,
    val updateWorkoutComponentsUseCase: UpdateWorkoutComponentsUseCase,
): ViewModel() {

    init {
        Log.d("MyWorkoutsScreenViewModel", "I was initialised")
    }

    var snackbarHostState = SnackbarHostState()
        private set

    var workoutApiRequestState: WorkoutAPIRequestState by mutableStateOf(WorkoutAPIRequestState.Ready)
        private set

    var buttonDelay by mutableStateOf(300L)
        private set

    // TODO -> When to use WorkoutForUI
    // Can either use empty object or null
    var workouts : SavedWorkouts by mutableStateOf(SavedWorkouts(workouts = listOf()))
        private set

    private var workoutsNonEditedCopy : SavedWorkouts = SavedWorkouts(workouts = listOf())

    var selectedWorkoutIdx : Int? by mutableStateOf(null)
        private set

    var editMode : Boolean by mutableStateOf(false)
        private set

    fun updateSelectedIdx(
        idx: Int,
    ) {
        selectedWorkoutIdx = idx
        Log.d("updateSelectedIdx", "New idx value is $selectedWorkoutIdx")
    }

    fun isIdxSelected(
        idx: Int,
    ): Boolean {

        // && to allow lazy evaluation to occur
        return (selectedWorkoutIdx != null) && (selectedWorkoutIdx == idx)

        // if (selectedWorkoutIdx == null) {
        //     return false
        // }
        // else if (selectedWorkoutIdx == idx) {
        //     return true
        // }

    }

    fun updateComponent(
        workoutIdx: Int,
        componentIdx: Int,
        newWorkoutComponent: WorkoutComponent,
    ) {

        Log.d("updateComponent", "Asked to update workout $workoutIdx, component $componentIdx, with $newWorkoutComponent")

        workouts = workouts.updateComponent(
            workoutIdx=workoutIdx,
            componentIdx=componentIdx,
            newWorkoutComponent = newWorkoutComponent,
        )
    }

    fun updateUserWorkouts() {

        workoutApiRequestState = WorkoutAPIRequestState.Loading

        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            // Makes the enable to disable transition of the button less jumpy looking
            delay(buttonDelay)

            val getUserWorkoutsResult = getUserWorkoutsUseCase()

            when (getUserWorkoutsResult) {
                is GetUserWorkoutsResult.Success -> {
                    // Continue after when block
                }
                is GetUserWorkoutsResult.Unauthenticated -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Unauthenticated
                    return@launch
                }
                else -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Error("There was a problem. Please try again in a few seconds")
                    return@launch
                }
            }

            // TODO -> Use UI ready version? Usecase can do the transformation, but shouldn't define this transformation as it's not business logic
            workouts = getUserWorkoutsResult.workouts
            workoutsNonEditedCopy = workouts.deepCopy()

            workoutApiRequestState = WorkoutAPIRequestState.Success("Workouts Loaded")

            // Here, or somewhere, update the state of the workouts to display, so that this new one
            // is displayed. Use API data, not the data that has been sent, to maintain a single source of truth.

        }

    }

    fun hasChangedWorkoutComponentChanged(
        workoutIdx: Int,
        componentIdx: Int,
    ) : Boolean {

        // Done -> Make getters and setters for both these objects
        val workoutComponent = workouts[workoutIdx, componentIdx]
        val originalWorkoutComponent = workoutsNonEditedCopy[workoutIdx, componentIdx]

        return workoutComponent != originalWorkoutComponent

    }

    // fun getChangedWorkoutComponents(): List<WorkoutComponent> {
    //
    //     var workoutComponents = listOf<WorkoutComponent>()
    //
    //     var targetIndexes = listOf<Pair<Int, Int>>()
    //     workouts.workouts.forEachIndexed { workoutIdx, workout ->
    //         workout.workout_components.forEachIndexed { componentIdx, workoutComponent ->
    //             val wasChanged = hasChangedWorkoutComponentChanged(
    //                 workoutIdx,
    //                 componentIdx,
    //             )
    //             if (wasChanged) {
    //                 targetIndexes = targetIndexes + Pair(workoutIdx, componentIdx)
    //             }
    //         }
    //     }
    //
    //     return targetIndexes.map { indexes ->
    //         workouts[indexes.first, indexes.second]
    //     }
    //
    // }

    fun getChangedWorkoutComponents(): List<WorkoutComponent> {
        return workouts.workouts.flatMapIndexed { workoutIdx, workout ->
            workout.workout_components.indices.filter { componentIdx ->
                hasChangedWorkoutComponentChanged(workoutIdx, componentIdx)
            }.map { componentIdx ->
                workouts[workoutIdx, componentIdx]
            }
        }
    }

    fun attemptToUpdateWorkoutComponents() {

        Log.d("attemptToUpdateWorkoutComponents", "Entering this function")

        workoutApiRequestState = WorkoutAPIRequestState.Loading

        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            // Makes the enable to disable transition of the button less jumpy looking
            delay(buttonDelay)

            val updateWorkoutComponentsResult = updateWorkoutComponentsUseCase(
                workoutComponents = getChangedWorkoutComponents(),
            )

            when (updateWorkoutComponentsResult) {
                is UpdateWorkoutComponentsResult.Success -> {
                    // Continue after when block
                }

                is UpdateWorkoutComponentsResult.Unauthenticated -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Unauthenticated
                    return@launch
                }

                else -> {
                    workoutApiRequestState =
                        WorkoutAPIRequestState.Error("There was a problem. Please try again in a few seconds")
                    return@launch
                }
            }

            // TODO -> Set workout API state to error if something occurred, and only continue if nothing did.

            updateUserWorkouts()

            workoutApiRequestState = WorkoutAPIRequestState.Success("Workouts Updated")

        }

    }

    fun switchEditMode() {

        if (editMode) {
            // Entering this as we are switching from edit to done
            Log.d("switchEditMode", "Updating...")
            attemptToUpdateWorkoutComponents()
        }
        else {

        }

        editMode = !editMode

        // If switching from edit, then attempt to save in the backend

        // If switching to edit, then populate the VM's edit copy. Actually, this copy should only be
        // reset when the workout list is grabbed from the API, so leave this to only updateWorkouts to manage.

        // App needs an identifier with the workout

    }

}