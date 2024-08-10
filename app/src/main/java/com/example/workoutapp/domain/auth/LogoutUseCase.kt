package com.example.workoutapp.domain.auth

import android.util.Log
import com.example.workoutapp.models.workoutapi.RefreshToken
import com.example.workoutapp.models.workoutapi.Salt
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class LogoutUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(

    ): Boolean {

        val result = appointmentsAPIRepository.logout()

        Log.d("LogoutUseCase", "Tokens and local token DB wiped")

        return result

    }

}

/**
 *
 */
// sealed class LogoutResult {
//     object Success : LoginResult()
//     object WrongCredentials : LoginResult()
//     // No InvalidCredentials here, will be handled by the UI already
//     // data class InvalidCredentials(val message: String) : LoginResult()
//     object Error : LoginResult()
// }