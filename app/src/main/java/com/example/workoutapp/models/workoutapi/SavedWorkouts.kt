package com.example.workoutapp.models.workoutapi

import com.example.workoutapp.models.helpers.DeepCopyable

/**
 * Currently just replaces using List directly, but in future the relevant endpoint payloads may contains
 * more than just a list.
 *
 * Specifies that Workouts contain WorkoutComponents that DO have IDs.
 *
 */
data class SavedWorkouts(
    var workouts : List<Workout<WorkoutComponent>>,
) : DeepCopyable<SavedWorkouts> {

    operator fun get(
        workoutIdx: Int,
        componentIdx: Int,
    ) : WorkoutComponent {

        return workouts[workoutIdx][componentIdx]

    }

    operator fun get(
        workoutIdx: Int,
    ) : Workout<WorkoutComponent> {

        return workouts[workoutIdx]

    }

    val size: Int
        get() = workouts.size

    fun updateComponent(
        workoutIdx: Int,
        componentIdx: Int,
        newWorkoutComponent: WorkoutComponent,
    ): SavedWorkouts {
        return this.copy(
            workouts=workouts.mapIndexed { wIdx, workout ->
                if (wIdx == workoutIdx) {
                    workout.copy(
                        workout_components = workout.workout_components.mapIndexed { wcIdx, workoutComponent ->
                            if (wcIdx == componentIdx) {
                                newWorkoutComponent
                            }
                            else workoutComponent
                        }
                    )
                } else workout
            },
        )
    }

    override fun deepCopy(): SavedWorkouts {
        return SavedWorkouts(
            workouts = workouts.map { it.deepCopy() },
        )
    }

}