package com.example.workoutapp.models.workoutapi

/**
 * A generic class which represents a response which just contains a payload
 */
data class WorkoutAPIGETResponse<T>(
    val payload: T
)