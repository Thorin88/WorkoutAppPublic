package com.example.workoutapp.models

import com.example.workoutapp.models.workoutapi.WorkoutComponent

// TODO -> Move to a different place

/**
 * (depreciated) Needs to be updated for new WorkoutComponent classes
 * An object that can be used to prepare a WorkoutComponent for viewing on the UI, decoupling the
 * need for the existing WorkoutComponent objects to be edited to have prettier attribute values.
 */
data class WorkoutComponentForUI private constructor (
    val exercise_name : String,
    val position : Int,
    val reps : String,
    val weight : Float,
    val units : String,
) {

    companion object Factory {

        fun fromWorkoutComponent(
            workoutComponent: WorkoutComponent,
        ): WorkoutComponentForUI {

            val createdInstance = WorkoutComponentForUI(
                exercise_name = workoutComponent.exercise_name,
                position = workoutComponent.position,
                reps = workoutComponent.reps,
                weight = workoutComponent.weight,
                units = workoutComponent.units,
            )

            return createdInstance

        }

    }

}
