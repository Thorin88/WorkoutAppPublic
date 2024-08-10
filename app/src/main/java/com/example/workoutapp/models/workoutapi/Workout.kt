package com.example.workoutapp.models.workoutapi

import com.example.workoutapp.models.helpers.DeepCopyable

/**
 *
 * The point of the heavily restricted type parameter is to have a Workout able to hold different types
 * of workout components (with and without IDs) without the need for a nested class for the component class.
 * This restriction limits us to the WorkoutComponentLike sealed class. This is more of a description regarding
 * the typing of Workout, since it doesn't expose any shared functionality. The deep copy is more important regarding
 * generic functionality, but this class isn't designed to be used with the generic WorkoutComponentLike.
 * This class's purpose is to prevent the need for an additional workout class per type of component.
 *
 * If the use of the component classes does diverge more significantly, then separete workout classes can
 * be made, or WorkoutComponent can be an interface.
 *
 * Needs DeepCopyable<T> compared to just cloneable because cloneable isn't guaranteed to return the
 * same type as the thing being cloned, via it's definition in interfaces.
 */
data class Workout<T>(
    val name : String,
    val ai_generated: Boolean,
    val workout_components : List<T>,
    // Could have a date here
) : DeepCopyable<Workout<T>> where T : DeepCopyable<T>, T : WorkoutComponentLike{

    operator fun get(
        componentIdx: Int,
    ): T {

        return workout_components[componentIdx]

    }

    val size: Int
        get() = workout_components.size

    override fun deepCopy(): Workout<T> {
        return Workout(
            name = name,
            ai_generated = ai_generated,
            workout_components = workout_components.map { it.deepCopy() }
        )
    }

}