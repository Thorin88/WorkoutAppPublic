package com.example.workoutapp.screens.helpers

fun makeNamePretty(name: String): String {
    val nameWithSpaces = name.replace("_", " ")
    return nameWithSpaces.split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}