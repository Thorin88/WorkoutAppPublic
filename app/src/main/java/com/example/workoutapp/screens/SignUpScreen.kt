package com.example.workoutapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import com.example.workoutapp.models.TextFieldState
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.navigation.NavSubGraphs
import com.example.workoutapp.ui.theme.WorkoutAppTheme
import com.example.workoutapp.viewmodels.SignUpScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent(
    navController: NavController,
    usernameFieldState: TextFieldState,
    onUsernameUpdate: (String) -> Unit,
    passwordFieldState: TextFieldState,
    onPasswordUpdate: (String) -> Unit,
    workoutApiRequestState: WorkoutAPIRequestState,
    signup: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    val uiIsEnabled = (workoutApiRequestState !is WorkoutAPIRequestState.Loading)

    if (workoutApiRequestState is WorkoutAPIRequestState.Error) {

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = workoutApiRequestState.message,
                withDismissAction = true
            )
        }
    }
    // In a separate conditional branch, as the compiler isn't able to know if the .message
    // attribute is present if we combine these conditions. The workaround that allows this would
    // mean that the APIRequestState gets a bit messy, and some code will be needed which is not completely
    // safe; requiring a cast at runtime.
    if (workoutApiRequestState is WorkoutAPIRequestState.Success) {

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
            // Nested Column. This is so that the items are aligned inside the space allocated
            // by the outer column to this column. Hence the weighting is applied to the column
            // and not the things inside the column directly.
            Column(
                // modifier = Modifier.weight(0.4f),
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
                    // trailingIcon = {
                    //     if (isError)
                    //         Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
                    // },
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
                    // trailingIcon = {
                    //     if (isError)
                    //         Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
                    // },
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // .weight(0.2f)
                ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        signup()
                    },
                    enabled = uiIsEnabled,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Sign Up")
                }
            }
            Box(modifier = Modifier
                .padding(16.dp)
                // .weight(0.1f),
            ) {
                if (workoutApiRequestState is WorkoutAPIRequestState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
            Button(
                onClick = {

                    navController.navigate(NavSubGraphs.AuthSubGraph.route) {
                        popUpTo(NavSubGraphs.AuthSubGraph.route) {
                            inclusive = true
                        }
                        // No launchTop here, as this pop will purge these screens anyway
                    }

                },
                enabled = uiIsEnabled,
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
                    // .weight(0.2f),
            ) {
                Text(text = "Back to Login")
            }

            // Text(
            //     text = "DEBUG: $workoutApiRequestState",
            //     modifier = Modifier.padding(16.dp)
            // )

        }

    }

}

@Composable
fun SignUpScreen(
    navController: NavController,
    signupScreenViewModel: SignUpScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier.fillMaxSize(),
) {

    SignUpScreenContent(
        navController = navController,
        usernameFieldState = signupScreenViewModel.usernameFieldState,
        onUsernameUpdate = { signupScreenViewModel.usernameFieldUpdated(it) },
        passwordFieldState = signupScreenViewModel.passwordFieldState,
        onPasswordUpdate = { signupScreenViewModel.passwordFieldUpdated(it) },
        workoutApiRequestState = signupScreenViewModel.apiRequestState,
        signup = { signupScreenViewModel.signup() },
        snackbarHostState = signupScreenViewModel.snackbarHostState,
        modifier = modifier,
    )

}

@Preview(showBackground = true)
@Composable
fun SignUpScreenContentPreview() {
    WorkoutAppTheme {
        SignUpScreenContent(
            navController = rememberNavController(),
            modifier = Modifier.size(300.dp),
            usernameFieldState = TextFieldState(text = "", isValid = true),
            onUsernameUpdate = {},
            passwordFieldState = TextFieldState(text = "", isValid = true),
            onPasswordUpdate = {},
            workoutApiRequestState = WorkoutAPIRequestState.Ready,
            signup = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}