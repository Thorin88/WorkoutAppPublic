package com.example.workoutapp.screens.helpers

class ExerciseDescriptions() {

    private val exerciseDescriptions = ExerciseContentConstants.exercises
        .map {
            it to "Description of $it"
        }
        .toMap()

    operator fun get(
        exerciseName: String,
    ): String {

        if (!exerciseDescriptions.containsKey(exerciseName)) {
            return "Description Unavailable"
        }
        else {
            return exerciseDescriptions[exerciseName]!!
        }

    }

}