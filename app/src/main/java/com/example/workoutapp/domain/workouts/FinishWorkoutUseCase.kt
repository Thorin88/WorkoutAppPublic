package com.example.workoutapp.domain.workouts

import android.util.Log
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class FinishWorkoutUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(
        workoutComponents: List<WorkoutComponent>,
    ): FinishWorkoutResult {

        val repoCallResult = appointmentsAPIRepository.finishWorkout(
            workoutComponents = workoutComponents,
        )

        when (repoCallResult) {

            is WorkoutAPIRepositoryResponse.Success -> {

                return FinishWorkoutResult.Success

            }

            is WorkoutAPIRepositoryResponse.Error -> {

                Log.d("FinishWorkoutUseCase", "Error Message from API: ${repoCallResult.code} ${repoCallResult.message}")
                // Creation of specific error subclass objects can be done here by having this usecase define
                // the message a user sees for certain error codes.
                return FinishWorkoutResult.Error

            }

            is WorkoutAPIRepositoryResponse.Unauthenticated -> {

                return FinishWorkoutResult.Unauthenticated

            }

            is WorkoutAPIRepositoryResponse.NetworkError -> {

                Log.d("FinishWorkoutUseCase", "Encountered Network Error")
                return FinishWorkoutResult.Error

            }

        }

    }

}

/**
 *
 */
sealed class FinishWorkoutResult {
    object Success : FinishWorkoutResult()
    object Unauthenticated : FinishWorkoutResult()
    object Error : FinishWorkoutResult()
}