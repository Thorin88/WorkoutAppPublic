package com.example.workoutapp.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.workouts.CreateWorkoutResult
import com.example.workoutapp.domain.workouts.CreateWorkoutUseCase
import com.example.workoutapp.domain.workouts.GetWorkoutRecommendationResult
import com.example.workoutapp.domain.workouts.GetWorkoutRecommendationUseCase
import com.example.workoutapp.models.TextFieldState
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.models.workoutapi.WorkoutComponentWithoutID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutScreenViewModel @Inject constructor (
    val createWorkoutUseCase : CreateWorkoutUseCase,
    val getWorkoutRecommendationUseCase : GetWorkoutRecommendationUseCase
): ViewModel() {

    init {
        Log.d("CreateWorkoutScreenViewModel", "I was initialised")
    }

    var snackbarHostState = SnackbarHostState()
        private set

    var workoutApiRequestState: WorkoutAPIRequestState by mutableStateOf(WorkoutAPIRequestState.Ready)
        private set

    var buttonDelay by mutableStateOf(300L)
        private set

    private val userQueryFieldStateStartingValue = ""
    var userQueryFieldState by mutableStateOf(
        value = TextFieldState(
            text = userQueryFieldStateStartingValue,
            isValid = true,
        )
    )
        private set

    // Preventing large costs from not limiting the input to Gemini
    private var messageCharacterLimit = 200

    fun userQueryFieldUpdated(enteredMessage: String) {
        if (enteredMessage.length < messageCharacterLimit) {
            userQueryFieldState = userQueryFieldState.copy(
                text = enteredMessage,
                isValid = true,
                message = "",
            )
        }
        else if (enteredMessage.length == messageCharacterLimit) {
            userQueryFieldState = userQueryFieldState.copy(
                text = enteredMessage,
                isValid = false,
                message = "Maximum Character Limit Reached",
            )
        }
        else {
            userQueryFieldState = userQueryFieldState.copy(
                isValid = false,
                message = "Maximum Character Limit Reached",
            )
        }
    }
    private fun resetUserQueryField() {
        userQueryFieldState = TextFieldState(
            text = userQueryFieldStateStartingValue,
            isValid = true,
        )
    }

    fun attemptToCreateWorkout() {

        // TODO -> Verify form fields, or have the UI control the values they can take
        // handleAndVerifySignUpFields() // Shows UI messages

        // if (!areLoginFieldsAreValid()) { // Prevents execution if fields are invalid

        workoutApiRequestState = WorkoutAPIRequestState.Loading

        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            // Makes the enable to disable transition of the button less jumpy looking
            delay(buttonDelay)

            Log.d("attemptToCreateWorkout", "Using hard-coded workout object")
            val exampleWorkoutComponents = listOf(
                WorkoutComponentWithoutID(
                    // workout_component_id = "dummy_id",
                    exercise_name = "bicep_curls",
                    reps = "10",
                    position = 0,
                    weight = 14.0f,
                    units = "kg"
                ),
                WorkoutComponentWithoutID(
                    // workout_component_id = "dummy_id",
                    exercise_name = "leg_press",
                    reps = "10",
                    position = 0,
                    weight = 55.0f,
                    units = "kg"
                ),
                WorkoutComponentWithoutID(
                    // workout_component_id = "dummy_id",
                    exercise_name = "flat_dumbell_press",
                    reps = "10",
                    position = 0,
                    weight = 20.0f,
                    units = "kg"
                ),
            )
            val workout = Workout(
                name = "Example Workout",
                ai_generated = false,
                workout_components = exampleWorkoutComponents,
            )

            val createWorkoutResult = createWorkoutUseCase(
                workout = workout,
            )

            when (createWorkoutResult) {
                is CreateWorkoutResult.Success -> {
                    // Continue after when block
                }
                is CreateWorkoutResult.Unauthenticated -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Unauthenticated
                    return@launch
                }
                else -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Error("There was a problem. Please try again in a few seconds")
                    return@launch
                }
            }

            workoutApiRequestState = WorkoutAPIRequestState.Success("Workout Created")

            // Here, or somewhere, update the state of the workouts to display, so that this new one
            // is displayed. Use API data, not the data that has been sent, to maintain a single source of truth.

        }

    }

    fun attemptToRequestWorkoutRecommendation() {

        // TODO -> Verify form fields, or have the UI control the values they can take
        // Empty user input will cause errors
        // handleAndVerifySignUpFields() // Shows UI messages

        // if (!areLoginFieldsAreValid()) { // Prevents execution if fields are invalid

        workoutApiRequestState = WorkoutAPIRequestState.Loading

        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            // Makes the enable to disable transition of the button less jumpy looking
            delay(buttonDelay)

            Log.d("attemptToCreateWorkout", "Using hard-coded workout object")

            val requestWorkoutRecommendationResult = getWorkoutRecommendationUseCase(
                // userQuery = "Can you suggest a workout of 6-8 exercises that targets my chest please?",
                userQuery = userQueryFieldState.text,
            )

            when (requestWorkoutRecommendationResult) {
                is GetWorkoutRecommendationResult.Success -> {
                    // Continue after when block
                }
                is GetWorkoutRecommendationResult.Unauthenticated -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Unauthenticated
                    return@launch
                }
                else -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Error("There was a problem. Please try again in a few seconds")
                    return@launch
                }
            }

            workoutApiRequestState = WorkoutAPIRequestState.Success(requestWorkoutRecommendationResult.message)

        }

    }

}