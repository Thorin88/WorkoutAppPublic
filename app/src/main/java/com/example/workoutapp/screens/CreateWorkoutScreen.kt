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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.R
import com.example.workoutapp.models.TextFieldState
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.screens.helpers.GeminiGradient
import com.example.workoutapp.screens.helpers.GeminiGradientStrong
import com.example.workoutapp.screens.helpers.GenericTopBar
import com.example.workoutapp.screens.helpers.MakeCard
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.CreateWorkoutScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkoutScreenContent(
    navController: NavController,

    workoutApiRequestState: WorkoutAPIRequestState,

    onCreateWorkoutClick : () -> Unit,

    userQueryFieldState: TextFieldState,
    onUserQueryFieldUpdate: (String) -> Unit,
    onGetWorkoutRecommendationClick : () -> Unit,

    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("HomeScreenContent", "I got recomposed")

    val pageIsLoading = workoutApiRequestState is WorkoutAPIRequestState.Loading

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            GenericTopBar(
                navController = navController,
                title = "Make a Workout",
                includeBackArrow = true,
            )
        }
    ) { contentPadding ->

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

            Text(text = "Create Example Workout")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onCreateWorkoutClick() },
                enabled = !pageIsLoading,
            ) {
                Text(text = "Create Workout")
            }

            Spacer(modifier = Modifier.height(32.dp))

            WorkoutSuggestionUI()

            TextField(
                value = userQueryFieldState.text,
                onValueChange = {
                    onUserQueryFieldUpdate(it)
                },
                label = { Text("Ask the AI to make you a workout...") },
                maxLines = 3,
                // enabled = TODO,
                isError = !userQueryFieldState.isValid,
                supportingText = {
                    if (!userQueryFieldState.isValid) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = userQueryFieldState.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onGetWorkoutRecommendationClick() },
                enabled = !pageIsLoading,
            ) {
                Text(text = "Create Workout")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (workoutApiRequestState is WorkoutAPIRequestState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
                return@Scaffold
            }

            if (workoutApiRequestState is WorkoutAPIRequestState.Success) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = "Workout created, check your 'My Workouts' page!",
                        withDismissAction = true
                    )
                }
            }

            if (workoutApiRequestState is WorkoutAPIRequestState.Error) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = "Something went wrong",
                        withDismissAction = true
                    )
                }
            }

        }

    }

}

@Composable
fun CreateWorkoutScreen(
    navController: NavController,
    createWorkoutScreenViewModel: CreateWorkoutScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    ScreenWithApiUsage(
        navController = navController,
        apiRequestState = createWorkoutScreenViewModel.workoutApiRequestState,
        content = {
            CreateWorkoutScreenContent(
                navController = navController,

                workoutApiRequestState = createWorkoutScreenViewModel.workoutApiRequestState,
                onCreateWorkoutClick = { createWorkoutScreenViewModel.attemptToCreateWorkout() },

                userQueryFieldState = createWorkoutScreenViewModel.userQueryFieldState,
                onUserQueryFieldUpdate = { createWorkoutScreenViewModel.userQueryFieldUpdated(it) },
                onGetWorkoutRecommendationClick = { createWorkoutScreenViewModel.attemptToRequestWorkoutRecommendation() },

                snackbarHostState = createWorkoutScreenViewModel.snackbarHostState,
                modifier = modifier,
            )
        },
    )

}

@Preview(showBackground = true)
@Composable
fun CreateWorkoutContentPreview() {
    WorkoutAppTheme {
        CreateWorkoutScreenContent(
            navController = rememberNavController(),

            workoutApiRequestState = WorkoutAPIRequestState.Ready,
            onCreateWorkoutClick = {},

            userQueryFieldState = TextFieldState("", isValid = true),
            onUserQueryFieldUpdate = {},
            onGetWorkoutRecommendationClick = {},

            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Composable
fun GeminiHeader() {
    // Define the gradient brush
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0000FF), // Blue
            Color(0xFF800080)  // Purple
        )
    )

    // Build the text with gradient applied to "Gemini"
    val headerText = buildAnnotatedString {
        append("Create with ")
        withStyle(style = SpanStyle(brush = gradientBrush)) {
            append("Gemini")
        }
    }

    // Wrap the Text in a Box
    Box(
        modifier = Modifier
            .padding(16.dp)
            // .background(Color.White) // Optional: Background color of the box
            .padding(16.dp) // Padding inside the box
    ) {
        Text(
            text = headerText,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun WorkoutSuggestionUI() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Creating a gradient brush for the "Gemini" text
        val gradientBrush = GeminiGradientStrong

        // Text with gradient on "Gemini"
        val headerText = buildAnnotatedString {
            append("Create with ")
            withStyle(style = SpanStyle(brush = gradientBrush)) {
                append("Gemini")
            }
        }

        // Displaying the header text with gradient
        Text(
            text = headerText,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sub-header Text
        Text(
            text = "Try asking:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // List of suggestions
        val suggestions = listOf(
            "Can you make me a leg workout with 5 exercises please?",
            "Can you up the intensity of the last workout I finished?",
            "Can you make me a workout that has a big focus on my shoulders?"
        )

        // Displaying suggestions
        suggestions.forEach { suggestion ->
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}