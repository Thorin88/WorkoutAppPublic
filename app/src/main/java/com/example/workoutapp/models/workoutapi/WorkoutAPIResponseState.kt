package com.example.workoutapp.models.workoutapi

/**
 * These values are used by Views. They are the top most level API state that the VMs can communicate
 * to the Views.
 *
 * The sealed interface means that we limit the values of this interface to these specific values. This
 * can make code involving conditions not require an else branch (since compiler is aware), if all cases are covered.
 *
 * These values are used with "is", which essentially checks if a value is an instance of these defined
 * classes. This is different to ==, as the object would need to be instantiated to do the comparison.
 *
 * This is an interface, as it makes sense that an instance of APIRequestState cannot be created, unlike
 * a sealed class, where the parent class can be instantiated.
 */
sealed interface WorkoutAPIRequestState {
    /**
     * Used as a flag to indicate that the page should be ready for user input before making a request
     */
    object Ready : WorkoutAPIRequestState
    data class Success(val message: String) : WorkoutAPIRequestState
    data class Error(val message: String) : WorkoutAPIRequestState
    /**
     * Indicates that we are still waiting on a response from the API
     */
    object Loading : WorkoutAPIRequestState
    /**
     * Indicates that the provided access token or refresh token was invalid or expired.
     */
    object Unauthenticated : WorkoutAPIRequestState
}