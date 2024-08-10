package com.example.workoutapp.screens.helpers

/**
 * No Dependency injection here, since we are in composables, which are intended to be stateless
 */
class ExerciseContent(
    exerciseName: String,
    iconMapper: IconMapper = IconMapper(),
    exerciseDescriptions: ExerciseDescriptions = ExerciseDescriptions(),
) {

    var exerciseIconPath = iconMapper[exerciseName]
    var exerciseDescription = exerciseDescriptions[exerciseName]

}