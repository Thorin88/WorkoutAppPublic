package com.example.workoutapp.navigation

sealed class Screen(val route: String) {

    object StartScreen : Screen(route = "start_screen")

    object LoginScreen : Screen(route = "login_screen")

    object SignUpScreen : Screen(route = "signup_screen")

    object HomeScreen : Screen(route = "home_screen")

    object LogoutScreen : Screen(route = "logout_screen")

    object CreateWorkoutScreen : Screen(route = "create_workout_screen")

    object MyWorkoutsScreen : Screen(route = "my_workouts_screen")

    object WorkoutScreen : Screen(route = "workout_screen")

    object TrackWorkoutScreen : Screen(route = "track_workout_screen")

    object TrackWorkoutSummaryScreen : Screen(route = "track_workout_summary_screen")

    object TrackWorkoutComponentScreen : Screen(route = "track_workout_component_screen")

}