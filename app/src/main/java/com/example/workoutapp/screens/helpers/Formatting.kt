package com.example.workoutapp.screens.helpers

/**
 * Needs to leave DP as this is considered a value Float, and hence removing this will result in
 * discrepancy between the user's intended input and the shown value.
 */
fun removeTrailingZeros(input: String): String {
    return input.replaceFirst(Regex("0+\$"), "")
}

fun removeTrailingZerosAndDP(input: String): String {
    return input.replaceFirst(Regex("\\.?0+\$"), "")
}