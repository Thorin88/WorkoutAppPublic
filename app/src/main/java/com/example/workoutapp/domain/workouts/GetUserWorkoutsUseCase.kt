package com.example.workoutapp.domain.workouts

import android.util.Log
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class GetUserWorkoutsUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(

    ): GetUserWorkoutsResult {

        val savedWorkoutsResult = appointmentsAPIRepository.getUserWorkouts()

        when (savedWorkoutsResult) {

            is WorkoutAPIRepositoryResponse.Success -> {

                if (savedWorkoutsResult.data == null) {
                    Log.d("CreateWorkoutUseCase", "Payload contents was null")
                    return GetUserWorkoutsResult.Error
                }

                return GetUserWorkoutsResult.Success(
                    workouts = savedWorkoutsResult.data,
                )

            }

            is WorkoutAPIRepositoryResponse.Error -> {

                Log.d("CreateWorkoutUseCase", "Error Message from API: ${savedWorkoutsResult.code} ${savedWorkoutsResult.message}")
                // Creation of specific error subclass objects can be done here by having this usecase define
                // the message a user sees for certain error codes.
                return GetUserWorkoutsResult.Error

            }

            is WorkoutAPIRepositoryResponse.Unauthenticated -> {

                return GetUserWorkoutsResult.Unauthenticated

            }

            is WorkoutAPIRepositoryResponse.NetworkError -> {

                Log.d("CreateWorkoutUseCase", "Encountered Network Error")
                return GetUserWorkoutsResult.Error

            }

        }

    }

}

/**
 *
 */
sealed class GetUserWorkoutsResult {
    data class Success(val workouts : SavedWorkouts) : GetUserWorkoutsResult()
    object Unauthenticated : GetUserWorkoutsResult()
    object Error : GetUserWorkoutsResult()
}