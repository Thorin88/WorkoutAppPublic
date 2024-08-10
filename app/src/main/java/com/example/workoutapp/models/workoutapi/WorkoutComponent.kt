package com.example.workoutapp.models.workoutapi

import com.example.workoutapp.models.helpers.DeepCopyable

data class WorkoutComponent(
    val workout_component_id : String,
    val exercise_name : String,
    val position : Int,
    val reps : String,
    val weight : Float,
    val units : String,
) : DeepCopyable<WorkoutComponent>, WorkoutComponentLike() {

    // Not needed as data classes already define the equals() function. so == is content based.
    // fun contentEquals(
    //     queryObject : WorkoutComponent,
    // ) : Boolean {
    //
    //     return (
    //         (workout_component_id == queryObject.workout_component_id) and
    //         (exercise_name == queryObject.exercise_name) and
    //         (position == queryObject.position) and
    //         (reps == queryObject.reps) and
    //         (weight == queryObject.weight) and
    //         (units == queryObject.units) and
    //         (true)
    //     )
    //
    // }

    override fun deepCopy(): WorkoutComponent {
        return this.copy()
    }

}
