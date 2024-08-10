package com.example.workoutapp.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutapp.domain.auth.SignUpResult
import com.example.workoutapp.domain.auth.SignUpUseCase
import com.example.workoutapp.domain.loginfields.HandleLoginFieldsUseCase
import com.example.workoutapp.domain.loginfields.HandlePasswordFieldUseCase
import com.example.workoutapp.domain.loginfields.HandleUsernameFieldUseCase
import com.example.workoutapp.models.TextFieldState
import com.example.workoutapp.models.workoutapi.WorkoutAPIRequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor (
    private val handleUsernameFieldUseCase: HandleUsernameFieldUseCase,
    private val handlePasswordFieldUseCase: HandlePasswordFieldUseCase,
    private val handleLoginFieldsUseCase: HandleLoginFieldsUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    init {
        Log.d("SignUpScreenViewModel", "I was initialised")
    }

    // The delay before work is started following a button press and "disable UI" state change.
    private val buttonDelay = 200L

    // Screen's snackbar state
    var snackbarHostState = SnackbarHostState()
        private set

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

    // API Request State
    var apiRequestState: WorkoutAPIRequestState by mutableStateOf(WorkoutAPIRequestState.Ready)
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

    // TODO -> Refactor this to be more reusable, or even as a usecase
    fun areSignUpFieldsAreValid(): Boolean {

        return (usernameFieldState.isValid && passwordFieldState.isValid)

    }

    fun signup() {

        // Doing this before changing the state to loading and firing off the work to be done
        handleAndVerifySignUpFields()
        if (!areSignUpFieldsAreValid()) {
            return
        }

        apiRequestState = WorkoutAPIRequestState.Loading
        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            delay(buttonDelay)

            val signUpResult = signUpUseCase(
                username = usernameFieldState.text,
                password = passwordFieldState.text,
            )

            when (signUpResult) {
                is SignUpResult.Success -> {
                    resetUsernameField()
                    resetPasswordField()
                    apiRequestState = WorkoutAPIRequestState.Success("Signup Successful")
                }

                is SignUpResult.UserAlreadyExists -> {
                    apiRequestState = WorkoutAPIRequestState.Error("This username already exists, please enter another")
                }

                is SignUpResult.Error -> {
                    // The responses from the repo are not propogated in order to avoid the user
                    // seeing confusing messages. Instead, the VM listens for standalone error classes
                    // which this usecase defines as particularly relevant.
                    apiRequestState = WorkoutAPIRequestState.Error("There was an issue signing up, please try again")
                }

                is SignUpResult.NetworkError -> {
                    apiRequestState = WorkoutAPIRequestState.Error("Please check your internet connection and try again")
                }

            }

        }

    }

}