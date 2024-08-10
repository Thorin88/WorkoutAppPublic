package com.example.workoutapp.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.auth.LoginResult
import com.example.workoutapp.domain.auth.LoginUseCase
import com.example.workoutapp.domain.loginfields.HandleLoginFieldsUseCase
import com.example.workoutapp.domain.loginfields.HandlePasswordFieldUseCase
import com.example.workoutapp.domain.loginfields.HandleUsernameFieldUseCase
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import com.example.workoutapp.models.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor (
    private val handleUsernameFieldUseCase: HandleUsernameFieldUseCase,
    private val handlePasswordFieldUseCase: HandlePasswordFieldUseCase,
    private val handleLoginFieldsUseCase: HandleLoginFieldsUseCase,
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    init {
        Log.d("LoginScreenViewModel", "I was initialised")
    }

    var snackbarHostState = SnackbarHostState()
        private set

    private var buttonDelay by mutableStateOf(200L)

    private val usernameFieldStartingValue = ""
    private val passwordFieldStartingValue = ""

    var usernameFieldState by mutableStateOf(
        value = TextFieldState(
            text = usernameFieldStartingValue,
            isValid = true,
        )
    )
        private set
    fun usernameFieldUpdated(enteredUsername: String) {
        usernameFieldState = usernameFieldState.copy(text = enteredUsername)
        handleAndVerifyUsernameField()
    }
    private fun resetUsernameField() {
        usernameFieldState = TextFieldState(
            text = usernameFieldStartingValue,
            isValid = true,
        )
    }

    var passwordFieldState by mutableStateOf(
        value = TextFieldState(
            text = passwordFieldStartingValue,
            isValid = true,
        )
    )
        private set

    fun passwordFieldUpdated(enteredPassword: String) {
        passwordFieldState = passwordFieldState.copy(text = enteredPassword)
        handleAndVerifyPasswordField()
    }
    private fun resetPasswordField() {
        passwordFieldState = TextFieldState(
            text = passwordFieldStartingValue,
            isValid = true,
        )
    }

    var workoutApiRequestState: WorkoutAPIRequestState by mutableStateOf(WorkoutAPIRequestState.Ready)
        private set
    var navigateAway by mutableStateOf(value = false)
        private set

    private fun handleAndVerifyUsernameField() {

        handleUsernameFieldUseCase(
            username = usernameFieldState.text,
            onUsernameInvalid = { message ->
                usernameFieldState = usernameFieldState.copy(
                    isValid = false,
                    message = message,
                )
            },
            onUsernameValid = {
                usernameFieldState = usernameFieldState.copy(isValid = true)
            },
        )

    }

    private fun handleAndVerifyPasswordField() {

        handlePasswordFieldUseCase(
            password = passwordFieldState.text,
            onPasswordInvalid = { message ->
                passwordFieldState = passwordFieldState.copy(
                    isValid = false,
                    message = message,
                )
            },
            onPasswordValid = {
                passwordFieldState = passwordFieldState.copy(isValid = true)
            },
        )

    }

    private fun handleAndVerifySignUpFields() {

        handleLoginFieldsUseCase(
            username = usernameFieldState.text,
            onUsernameInvalid = { message ->
                usernameFieldState = usernameFieldState.copy(
                    isValid = false,
                    message = message,
                )
            },
            onUsernameValid = {
                usernameFieldState = usernameFieldState.copy(isValid = true)
            },
            password = passwordFieldState.text,
            onPasswordInvalid = { message ->
                passwordFieldState = passwordFieldState.copy(
                    isValid = false,
                    message = message,
                )
            },
            onPasswordValid = {
                passwordFieldState = passwordFieldState.copy(isValid = true)
            },
        )

    }

    fun areLoginFieldsAreValid(): Boolean {

        return (usernameFieldState.isValid && passwordFieldState.isValid)

    }

    fun attemptLogin() {

        // Doing this before changing the state to loading and firing off the work to be done
        handleAndVerifySignUpFields()
        if (!areLoginFieldsAreValid()) {
            Log.d("attemptLogin", "Login fields were invalid")
            return
        }

        workoutApiRequestState = WorkoutAPIRequestState.Loading

        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            // Makes the enable to disable transition of the button less jumpy looking
            delay(buttonDelay)

            val loginResult = loginUseCase(
                username = usernameFieldState.text,
                password = passwordFieldState.text,
            )

            when (loginResult) {
                is LoginResult.Success -> {
                    // Continue after when block
                }
                is LoginResult.WrongCredentials -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Error("Wrong username or password")
                    return@launch
                }
                else -> {
                    workoutApiRequestState = WorkoutAPIRequestState.Error("There was a problem. Please try logging in again")
                    return@launch
                }
            }

            // Not setting updating workoutApiRequestState, in order to keep the page loading without flickering

            // Instead indicating to navigateAway from the Login page
            navigateAway = true

        }

    }

    fun onSuccessfulLogin() {

        Log.d("LoginScreenViewModel.onSuccessfulLogin", "Called")

        // resetUsernameField()
        // resetPasswordField()

        navigateAway = false

    }

}