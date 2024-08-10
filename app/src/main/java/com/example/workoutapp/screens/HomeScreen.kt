package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.R
import com.example.workoutapp.navigation.NavSubGraphs
import com.example.workoutapp.navigation.Screen
import com.example.workoutapp.screens.helpers.GenericTopBar
import com.example.workoutapp.screens.helpers.MakeCard
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("HomeScreenContent", "I got recomposed")

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            GenericTopBar(
                navController = navController,
                title = "Home",
                includeBackArrow = false,
            )
        }
    ) { contentPadding ->

        // https://stackoverflow.com/questions/69394543/fillmaxsize-modifier-not-working-when-combined-with-verticalscroll-in-jetpack-co
        // Column + verticalScroll
        // This setup means that the height of the column is infinite. Hence, any reference to
        // max height seems to become contents height.
        // Fine to use, but max height as a concept does not exist.
        // LazyColumn + fillParentMaxHeight
        // An alternative, Column takes up an amount of space, and doesn't load all the things in
        // it, only what fits in this space.
        // Pager
        // Not scrolling, content is split into pages

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()) // Ensures content is still visible if screen is small
            ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // TODO -> This page could have a + here too, which also goes to the Make a workout page
            // TODO -> Could also have a start button, that takes you to a similar place
            MakeCard(
                title = "My Workouts",
                imageResourceID = R.drawable.my_workouts,
                onClick = {
                    navController.navigate(NavSubGraphs.MyWorkoutsSubGraph.route) {
                        // Nothing to do here
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MakeCard(
                title = "Make a Workout",
                imageResourceID = R.drawable.create_workout,
                onClick = {
                    navController.navigate(Screen.CreateWorkoutScreen.route) {
                        // Nothing to do here
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MakeCard(
                title = "Track a Workout",
                imageResourceID = R.drawable.track_workout,
                onClick = {
                    navController.navigate(NavSubGraphs.TrackWorkoutSubGraph.route) {
                        // Nothing to do here
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

        }

    }

}

@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    HomeScreenContent(
        navController = navController,
        snackbarHostState = homeScreenViewModel.snackbarHostState,
        modifier = modifier,
    )

}

@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    WorkoutAppTheme {
        HomeScreenContent(
            navController = rememberNavController(),
            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}