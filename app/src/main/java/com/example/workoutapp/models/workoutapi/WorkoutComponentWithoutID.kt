package com.example.workoutapp.models.workoutapi

import com.example.workoutapp.models.helpers.DeepCopyable

/**
 * Used when creating Workouts, since we don't need an ID in this instance.
 */
data class WorkoutComponentWithoutID(
    val exercise_name : String,
    val position : Int,
    val reps : String,
    val weight : Float,
    val units : String,
) : DeepCopyable<WorkoutComponentWithoutID>, WorkoutComponentLike() {

    override fun deepCopy(): WorkoutComponentWithoutID {
        // Since non nested types, shallow copy is sufficient
        return this.copy()
    }

}