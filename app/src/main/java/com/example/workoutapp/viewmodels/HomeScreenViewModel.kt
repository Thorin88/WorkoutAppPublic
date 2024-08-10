package com.example.workoutapp.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor (

): ViewModel() {

    init {
        Log.d("HomeScreenViewModel", "I was initialised")
    }

    var snackbarHostState = SnackbarHostState()
        private set

}