package com.example.workoutapp.navigation

sealed class NavSubGraphs(val startScreen: Screen, val route: String) {

    object StartSubGraph : NavSubGraphs(Screen.StartScreen, "start_subgraph") // No constructor as it just uses super()

    object AuthSubGraph : NavSubGraphs(Screen.LoginScreen, "auth_subgraph")

    object HomeSubGraph : NavSubGraphs(Screen.HomeScreen, "home_subgraph")

    object LogoutSubGraph : NavSubGraphs(Screen.LogoutScreen, "logout_subgraph")

    object MyWorkoutsSubGraph : NavSubGraphs(Screen.MyWorkoutsScreen, "my_workouts_subgraph")

    object TrackWorkoutSubGraph : NavSubGraphs(Screen.TrackWorkoutScreen, "track_workout_subgraph")

}