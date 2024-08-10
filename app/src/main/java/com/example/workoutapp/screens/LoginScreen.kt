package com.example.workoutapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.models.TextFieldState
import com.example.workoutapp.navigation.NavSubGraphs
import com.example.workoutapp.navigation.Screen
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.LoginScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    navController: NavController,

    usernameFieldState: TextFieldState,
    onUsernameUpdate: (String) -> Unit,
    passwordFieldState: TextFieldState,
    onPasswordUpdate: (String) -> Unit,

    workoutApiRequestState: WorkoutAPIRequestState,

    login: () -> Unit,
    onSuccessfulLogin: () -> Unit,
    navigateAway: Boolean,

    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    Log.d("LoginScreenContent", "I got recomposed")

    val uiIsEnabled = (workoutApiRequestState !is WorkoutAPIRequestState.Loading)

    if (navigateAway) {

        Log.d("LoginScreenContent", "Successful Login, attempting to navigate to next screen")

        // Changes navigateAway to back to false, to avoid navigation rerunning this section
        // as navigate will recompose the current composable.
        onSuccessfulLogin()

        navController.navigate(NavSubGraphs.HomeSubGraph.route) {
            popUpTo(NavSubGraphs.AuthSubGraph.route) {
                inclusive = true // All login data purged. The only way back is via another page, not the backstack.
            }
        }

        // No return here, instead the state initiating the navigation is equivalent to the state that
        // was before the state change, in terms of how the UI looks. This prevents a jumpy UI.

    }

    if (workoutApiRequestState is WorkoutAPIRequestState.Error) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = workoutApiRequestState.message,
                withDismissAction = true
            )
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->

        Column(
            modifier = modifier.padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(
                // modifier = Modifier
                //     .weight(0.4f)
                //     .fillMaxSize()
                // ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = usernameFieldState.text,
                    onValueChange = {
                        onUsernameUpdate(it)
                    },
                    label = { Text("Username") },
                    maxLines = 1,
                    enabled = uiIsEnabled,
                    isError = !usernameFieldState.isValid,
                    supportingText = {
                        if (!usernameFieldState.isValid) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = usernameFieldState.message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                )
                TextField(
                    value = passwordFieldState.text,
                    onValueChange = {
                        onPasswordUpdate(it)
                    },
                    label = { Text("Password") },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = uiIsEnabled,
                    isError = !passwordFieldState.isValid,
                    supportingText = {
                        if (!passwordFieldState.isValid) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = passwordFieldState.message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            Row(
                // modifier = Modifier
                //     .fillMaxSize()
                //     .weight(0.2f)
                // ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.SignUpScreen.route) {
                            // Nothing to do here, just add this screen to the stack
                        }
                    },
                    enabled = uiIsEnabled,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Sign Up")
                }
                Button(
                    onClick = {
                        login()
                    },
                    enabled = uiIsEnabled,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Login")
                }
            }

            Box(modifier = Modifier
                .padding(16.dp)
                // .weight(0.1f),
            ) {
                if (!uiIsEnabled) {
                    CircularProgressIndicator(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            // Text(
            //     text = "DEBUG: $workoutApiRequestState",
            //     modifier = Modifier.padding(16.dp)
            // )

        }

    }

}

@Composable
fun LoginScreen(
    navController: NavController,
    loginScreenViewModel: LoginScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    // TODO -> See if there's better practice for avoiding having 1 million things passed here (chatgpt may know)
    LoginScreenContent(
        navController = navController,

        usernameFieldState = loginScreenViewModel.usernameFieldState,
        onUsernameUpdate = { loginScreenViewModel.usernameFieldUpdated(it) },
        passwordFieldState = loginScreenViewModel.passwordFieldState,
        onPasswordUpdate = { loginScreenViewModel.passwordFieldUpdated(it) },
        workoutApiRequestState = loginScreenViewModel.workoutApiRequestState,
        login = { loginScreenViewModel.attemptLogin() },
        onSuccessfulLogin = { loginScreenViewModel.onSuccessfulLogin() },
        navigateAway = loginScreenViewModel.navigateAway,

        snackbarHostState = loginScreenViewModel.snackbarHostState,
        modifier = modifier,
    )

}

@Preview(showBackground = true)
@Composable
fun LoginScreenContentPreview() {
    WorkoutAppTheme {
        LoginScreenContent(
            navController = rememberNavController(),

            usernameFieldState = TextFieldState(text = "", isValid = true),
            onUsernameUpdate = {},
            passwordFieldState = TextFieldState(text = "", isValid = true),
            onPasswordUpdate = {},
            workoutApiRequestState = WorkoutAPIRequestState.Ready,
            login = {},
            onSuccessfulLogin = {},
            navigateAway = false,

            modifier = Modifier.size(300.dp),
            snackbarHostState = SnackbarHostState(),
        )
    }
}