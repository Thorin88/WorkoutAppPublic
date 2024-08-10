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
import com.example.workoutapp.domain.auth.LogoutUseCase
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
class LogoutScreenViewModel @Inject constructor (
    private val logoutUseCase: LogoutUseCase,
): ViewModel() {

    init {
        Log.d("LogoutScreenViewModel", "I was initialised")
    }

    var snackbarHostState = SnackbarHostState()
        private set

    private var pageDelay by mutableStateOf(500L)
        private set

    // Needs to be loading, since this is how the launchedEffect is started.
    var workoutApiRequestState: WorkoutAPIRequestState by mutableStateOf(WorkoutAPIRequestState.Loading)
        private set

    var navigateAway by mutableStateOf(value = false)
        private set

    fun attemptLogout() {

        workoutApiRequestState = WorkoutAPIRequestState.Loading

        // Coroutine that will do some work, then turn the UI back on via a state update.
        viewModelScope.launch {

            delay(pageDelay)

            Log.d("attemptLogout", "Started logout process")
            val result = logoutUseCase()

            Log.d("attemptLogout", "Finished logout process, setting navigateAway to true")
            navigateAway = true

            // Not changing loading state here since to make the navigation look smooth, we want
            // no states to change besides the navigation related one.

        }

    }

    fun onSuccessfulLogout() {

        Log.d("LogoutScreenViewModel.onSuccessfulLogin", "Called")

        navigateAway = false

    }

}