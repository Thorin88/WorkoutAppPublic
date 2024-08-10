package com.example.workoutapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.workoutapp.screens.CreateWorkoutScreen
import com.example.workoutapp.screens.HomeScreen
import com.example.workoutapp.screens.StartScreen
import com.example.workoutapp.screens.LoginScreen
import com.example.workoutapp.screens.LogoutScreen
import com.example.workoutapp.screens.MyWorkoutsScreen
import com.example.workoutapp.screens.SignUpScreen
import com.example.workoutapp.screens.TrackWorkoutComponentScreen
import com.example.workoutapp.screens.TrackWorkoutSummaryScreen
import com.example.workoutapp.screens.TrackWorkoutsScreen
import com.example.workoutapp.screens.WorkoutScreen
import com.example.workoutapp.viewmodels.MyWorkoutsScreenViewModel
import com.example.workoutapp.viewmodels.TrackWorkoutsScreenViewModel

@Composable
fun defineAndStartNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavSubGraphs.StartSubGraph.route) {

        // Home Subgraph
        navigation(
            startDestination = NavSubGraphs.StartSubGraph.startScreen.route,
            route = NavSubGraphs.StartSubGraph.route
        ) {
            composable(
                route = NavSubGraphs.StartSubGraph.startScreen.route
            ) { entry ->
                StartScreen(navController = navController)
            }
        }

        // Auth Subgraph
        navigation(
            startDestination = NavSubGraphs.AuthSubGraph.startScreen.route,
            route = NavSubGraphs.AuthSubGraph.route
        ) {
            composable(
                route = NavSubGraphs.AuthSubGraph.startScreen.route
            ) { entry ->
                LoginScreen(navController = navController)
            }
            composable(
                route = Screen.SignUpScreen.route
            ) { entry ->
                SignUpScreen(navController = navController)
            }
        }

        // Home Subgraph
        navigation(
            startDestination = NavSubGraphs.HomeSubGraph.startScreen.route,
            route = NavSubGraphs.HomeSubGraph.route
        ) {
            composable(
                route = NavSubGraphs.HomeSubGraph.startScreen.route
            ) { entry ->
                HomeScreen(navController = navController)
            }

            // TODO -> Could be another subgraph?
            composable(
                route = Screen.CreateWorkoutScreen.route
            ) { entry ->
                CreateWorkoutScreen(navController = navController)
            }

        }

        // MyWorkouts Subgraph
        navigation(
            startDestination = NavSubGraphs.MyWorkoutsSubGraph.startScreen.route,
            route = NavSubGraphs.MyWorkoutsSubGraph.route
        ) {
            composable(
                route = NavSubGraphs.MyWorkoutsSubGraph.startScreen.route
            ) { entry ->
                // Scoped to the subgraph. Each other composable needs to declare this too, but instances
                // will be reused if one already exists.
                val myWorkoutsScreenSharedViewModel = entry.sharedViewModel<MyWorkoutsScreenViewModel>(navController = navController)
                MyWorkoutsScreen(
                    navController = navController,
                    myWorkoutScreenViewModel = myWorkoutsScreenSharedViewModel,
                )
            }
            composable(
                route = Screen.WorkoutScreen.route
            ) { entry ->
                // Scoped to the subgraph. Each other composable needs to declare this too, but instances
                // will be reused if one already exists.
                val myWorkoutsScreenSharedViewModel = entry.sharedViewModel<MyWorkoutsScreenViewModel>(navController = navController)
                WorkoutScreen(
                    navController = navController,
                    myWorkoutsScreenViewModel = myWorkoutsScreenSharedViewModel,
                )
            }

            // TODO -> Other screens? Like a screen to view something? Though if this were reused
            // it would need a shared view model everytime?

        }

        navigation(
            startDestination = NavSubGraphs.TrackWorkoutSubGraph.startScreen.route,
            route = NavSubGraphs.TrackWorkoutSubGraph.route
        ) {
            composable(
                route = NavSubGraphs.TrackWorkoutSubGraph.startScreen.route
            ) { entry ->
                // Scoped to the subgraph. Each other composable needs to declare this too, but instances
                // will be reused if one already exists.
                val trackWorkoutsScreenSharedViewModel = entry.sharedViewModel<TrackWorkoutsScreenViewModel>(navController = navController)
                TrackWorkoutsScreen(
                    navController = navController,
                    trackWorkoutsScreenViewModel = trackWorkoutsScreenSharedViewModel,
                )
            }
            composable(
                route = Screen.TrackWorkoutSummaryScreen.route
            ) { entry ->
                val trackWorkoutsScreenSharedViewModel = entry.sharedViewModel<TrackWorkoutsScreenViewModel>(navController = navController)
                TrackWorkoutSummaryScreen(
                    navController = navController,
                    trackWorkoutsScreenViewModel = trackWorkoutsScreenSharedViewModel,
                )
            }
            composable(
                route = Screen.TrackWorkoutComponentScreen.route
            ) { entry ->
                val trackWorkoutsScreenSharedViewModel = entry.sharedViewModel<TrackWorkoutsScreenViewModel>(navController = navController)
                TrackWorkoutComponentScreen(
                    navController = navController,
                    trackWorkoutsScreenViewModel = trackWorkoutsScreenSharedViewModel,
                )
            }

        }

        // Logout Subgraph
        navigation(
            startDestination = NavSubGraphs.LogoutSubGraph.startScreen.route,
            route = NavSubGraphs.LogoutSubGraph.route
        ) {
            composable(
                route = NavSubGraphs.LogoutSubGraph.startScreen.route
            ) { entry ->
                LogoutScreen(navController = navController)
            }
        }

    }
}

/**
 * A helper function that creates and scopes a ViewModel of type T to the nested subgraph it is being
 * called in (derived from the NavBackStackEntry object that this function was called using).
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}