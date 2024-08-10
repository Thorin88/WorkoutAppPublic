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
class LoginUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
    private val getSaltUseCase: GetSaltUseCase,
    private val hashPasswordUseCase: HashPasswordUseCase,
) {

    suspend operator fun invoke(
        username: String,
        password: String,
    ): LoginResult {

        // TODO - reuse the Username and password verification usecases

        // Doesn't really need to be a usecase, but an example of a nested usecase
        val saltResult = getSaltUseCase(
            username = username,
        )

        var salt : Salt
        when (saltResult) {
            is GetSaltResult.Error -> {
                return LoginResult.Error
            }
            // Hides the fact that this username does not exist
            is GetSaltResult.NoSuchUser -> {
                return LoginResult.WrongCredentials
            }
            is GetSaltResult.Success -> {
                salt = saltResult.salt
            }
        }

        // TODO -> Make this a UseCase?
        val (hash, _) = hashPasswordUseCase(
            password = password,
            salt = salt.salt,
        )

        val loginResponse = appointmentsAPIRepository.login(
            username = username,
            hash = hash,
        )

        Log.d("LoginUseCase", "Login response: $loginResponse")

        val refreshToken : RefreshToken?
        when (loginResponse) {
            is WorkoutAPIRepositoryResponse.Success -> {
                refreshToken = loginResponse.data
            }
            is WorkoutAPIRepositoryResponse.Error -> {
                return if (loginResponse.code == 403) {
                    LoginResult.WrongCredentials
                } else {
                    LoginResult.Error
                }
            }
            else -> {
                return LoginResult.Error
            }
        }

        loginResponse.data // Should be possible, as Success is the only one not returning

        if (refreshToken == null) {
            Log.d("LoginUseCase", "Refresh Token was null (shouldn't happen)")
            return LoginResult.Error
        }

        return LoginResult.Success

    }

}

/**
 *
 */
sealed class LoginResult {
    object Success : LoginResult()
    object WrongCredentials : LoginResult()
    // No InvalidCredentials here, will be handled by the UI already
    // data class InvalidCredentials(val message: String) : LoginResult()
    object Error : LoginResult()
}