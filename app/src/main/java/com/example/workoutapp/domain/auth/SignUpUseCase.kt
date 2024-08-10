package com.example.workoutapp.domain.auth

import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 *
 */
class SignUpUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(
        username: String,
        password: String,
    ): SignUpResult {

        val response = appointmentsAPIRepository.signUp(
            username = username,
            password = password,
        )

        when (response) {
            is WorkoutAPIRepositoryResponse.Success -> {
                // Exit when block
            }

            is WorkoutAPIRepositoryResponse.Error -> {
                if (response.code == 409) {
                    return SignUpResult.UserAlreadyExists
                }
                return SignUpResult.Error
            }

            is WorkoutAPIRepositoryResponse.Unauthenticated -> {
                return SignUpResult.Error
            }

            is WorkoutAPIRepositoryResponse.NetworkError -> {
                return SignUpResult.NetworkError
            }

        }

        return SignUpResult.Success

    }

}

/**
 * These objects do not contain messages, since the ViewModel will decide on the message to show
 * based on the object returned. The ViewModel is responsible for deciding how to display any issues
 * to the user.
 */
sealed class SignUpResult {
    object Success : SignUpResult()
    object UserAlreadyExists : SignUpResult()
    // No invalid credentials field here, this will be managed by the UI
    object NetworkError : SignUpResult()
    object Error : SignUpResult()
}