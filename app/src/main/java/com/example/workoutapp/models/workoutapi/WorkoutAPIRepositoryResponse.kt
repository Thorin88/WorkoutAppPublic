package com.example.workoutapp.models.workoutapi

// A good example of this type of class is here: https://github.com/mitchtabian/Clean-Notes/blob/master/app/src/main/java/com/codingwithmitch/cleannotes/business/data/network/ApiResult.kt

/**
 * The set of response objects that the workout API repository can return. These objects
 * are then intended to be converted to equivalent states that the Views know how to handle.
 */
sealed class WorkoutAPIRepositoryResponse<out T : Any?> {

    data class Success<out T>(val data: T) : WorkoutAPIRepositoryResponse<T>()
    data class Error(val code: Int?, val message: String?) : WorkoutAPIRepositoryResponse<Nothing>()
    // No Loading option here, as that is only applicable in the context of UI state
    object Unauthenticated : WorkoutAPIRepositoryResponse<Nothing>()
    object NetworkError : WorkoutAPIRepositoryResponse<Nothing>()

}