package com.example.workoutapp.screens.helpers

import com.example.workoutapp.R

val MISSING_DRAWABLE : Int = R.drawable.missing

class IconMapper() {

    // private val exerciseDrawableMap = mapOf(
    //     "flat_dumbell_press" to R.drawable.flat_dumbell_press,
    //     "incline_dumbell_press" to R.drawable.incline_dumbell_press,
    //     "forward_dumbell_raises" to R.drawable.forward_dumbell_raises,
    //     "shrugs" to R.drawable.shrugs,
    //     "dumbell_row" to R.drawable.dumbell_row,
    //     "lateral_raises" to R.drawable.lateral_raises,
    //     "lat_pull_downs" to R.drawable.lat_pull_downs,
    //     "tricep_pulldowns" to R.drawable.tricep_pulldowns,
    //     "chest_fly" to R.drawable.chest_fly,
    //     "reverse_chest_fly" to R.drawable.reverse_chest_fly,
    //     "seated_rows" to R.drawable.seated_rows,
    //     "bicep_curls" to R.drawable.bicep_curls,
    //     "squats" to R.drawable.squats,
    //     "pistol_squats" to R.drawable.pistol_squats,
    //     "romanian_deadlifts" to R.drawable.romanian_deadlifts,
    //     "leg_press" to R.drawable.leg_press,
    //     "lunges" to R.drawable.lunges,
    //     "leg_curls" to R.drawable.leg_curls,
    //     "leg_extensions" to R.drawable.leg_extensions,
    //     "dips" to R.drawable.dips,
    //     "push_ups" to R.drawable.push_ups
    // )

    private val exerciseMapping = mapOf(
        "flat_dumbell_press" to R.drawable.chest,
        "incline_dumbell_press" to R.drawable.chest,
        "forward_dumbell_raises" to R.drawable.chest,
        "shrugs" to R.drawable.arm,
        "dumbell_row" to R.drawable.back,
        "lateral_raises" to R.drawable.arm,
        "lat_pull_downs" to R.drawable.back,
        "tricep_pulldowns" to R.drawable.arm,
        "chest_fly" to R.drawable.chest,
        "reverse_chest_fly" to R.drawable.chest,
        "seated_rows" to R.drawable.back,
        "bicep_curls" to R.drawable.arm,
        "squats" to R.drawable.leg,
        "pistol_squats" to R.drawable.leg,
        "romanian_deadlifts" to R.drawable.leg,
        "leg_press" to R.drawable.leg,
        "lunges" to R.drawable.leg,
        "leg_curls" to R.drawable.leg,
        "leg_extensions" to R.drawable.leg,
        "dips" to R.drawable.arm,
        "push_ups" to R.drawable.arm
    )

    // private val exerciseMapping : Map<String, Int> = ExerciseContentConstants.exercises
    //     .map {
    //         it to MISSING_DRAWABLE
    //     }
    //     .toMap()

    operator fun get(
        exerciseName: String,
    ): Int {

        if (!exerciseMapping.containsKey(exerciseName)) {
            return MISSING_DRAWABLE
        }
        else {
            return exerciseMapping[exerciseName]!!
        }

    }

}