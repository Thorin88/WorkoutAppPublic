package com.example.workoutapp.models.workoutapi

data class SignUp(
    val username : String,
    val hash : String,
    val salt : String,
)
