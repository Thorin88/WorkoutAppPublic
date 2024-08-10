package com.example.workoutapp.models

/**
 * Additionally encapsulates state values for the field's validity and an optional message, intended
 * to be displayed alongside the field when desired.
 */
data class TextFieldState(
    val text : String,
    val isValid : Boolean,
    val message : String = "",
)
