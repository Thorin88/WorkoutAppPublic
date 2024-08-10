package com.example.workoutapp.domain.workouts

import android.util.Log
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.models.workoutapi.WorkoutComponentWithoutID
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class UpdateWorkoutComponentsUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(
        workoutComponents: List<WorkoutComponent>,
    ): UpdateWorkoutComponentsResult {

        val creationResult = appointmentsAPIRepository.updateWorkoutComponents(
            workoutComponents = workoutComponents,
        )

        when (creationResult) {

            is WorkoutAPIRepositoryResponse.Success -> {

                return UpdateWorkoutComponentsResult.Success

            }

            is WorkoutAPIRepositoryResponse.Error -> {

                Log.d("UpdateWorkoutComponentsResult", "Error Message from API: ${creationResult.code} ${creationResult.message}")
                // Creation of specific error subclass objects can be done here by having this usecase define
                // the message a user sees for certain error codes.
                return UpdateWorkoutComponentsResult.Error

            }

            is WorkoutAPIRepositoryResponse.Unauthenticated -> {

                return UpdateWorkoutComponentsResult.Unauthenticated

            }

            is WorkoutAPIRepositoryResponse.NetworkError -> {

                Log.d("CreateWorkoutUseCase", "Encountered Network Error")
                return UpdateWorkoutComponentsResult.Error

            }

        }

    }

}

/**
 *
 */
sealed class UpdateWorkoutComponentsResult {
    object Success : UpdateWorkoutComponentsResult()
    object Unauthenticated : UpdateWorkoutComponentsResult()
    object Error : UpdateWorkoutComponentsResult()
}