package com.example.workoutapp.domain.workouts

import android.util.Log
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponentWithoutID
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class CreateWorkoutUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(
        workout: Workout<WorkoutComponentWithoutID>,
    ): CreateWorkoutResult {

        // Doesn't really need to be a usecase, but an example of a nested usecase
        val creationResult = appointmentsAPIRepository.createWorkout(
            workout = workout,
        )

        when (creationResult) {

            is WorkoutAPIRepositoryResponse.Success -> {

                return CreateWorkoutResult.Success

            }

            is WorkoutAPIRepositoryResponse.Error -> {

                Log.d("CreateWorkoutUseCase", "Error Message from API: ${creationResult.code} ${creationResult.message}")
                // Creation of specific error subclass objects can be done here by having this usecase define
                // the message a user sees for certain error codes.
                return CreateWorkoutResult.Error

            }

            is WorkoutAPIRepositoryResponse.Unauthenticated -> {

                return CreateWorkoutResult.Unauthenticated

            }

            is WorkoutAPIRepositoryResponse.NetworkError -> {

                Log.d("CreateWorkoutUseCase", "Encountered Network Error")
                return CreateWorkoutResult.Error

            }

        }

    }

}

/**
 *
 */
sealed class CreateWorkoutResult {
    object Success : CreateWorkoutResult()
    object Unauthenticated : CreateWorkoutResult()
    object Error : CreateWorkoutResult()
}