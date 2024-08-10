package com.example.workoutapp.domain.workouts

import android.util.Log
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class GetWorkoutRecommendationUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(
        userQuery: String,
    ): GetWorkoutRecommendationResult {

        val recommendationResult = appointmentsAPIRepository.requestWorkoutRecommendation(
            userQuery = userQuery,
        )

        when (recommendationResult) {

            is WorkoutAPIRepositoryResponse.Success -> {

                return GetWorkoutRecommendationResult.Success(message=recommendationResult.data?.ai_message.toString())

            }

            is WorkoutAPIRepositoryResponse.Error -> {

                Log.d("CreateWorkoutUseCase", "Error Message from API: ${recommendationResult.code} ${recommendationResult.message}")
                // Creation of specific error subclass objects can be done here by having this usecase define
                // the message a user sees for certain error codes.
                return GetWorkoutRecommendationResult.Error

            }

            is WorkoutAPIRepositoryResponse.Unauthenticated -> {

                return GetWorkoutRecommendationResult.Unauthenticated

            }

            is WorkoutAPIRepositoryResponse.NetworkError -> {

                Log.d("CreateWorkoutUseCase", "Encountered Network Error")
                return GetWorkoutRecommendationResult.Error

            }

        }

    }

}

/**
 *
 */
sealed class GetWorkoutRecommendationResult {
    data class Success(val message: String) : GetWorkoutRecommendationResult()
    object Unauthenticated : GetWorkoutRecommendationResult()
    object Error : GetWorkoutRecommendationResult()
}