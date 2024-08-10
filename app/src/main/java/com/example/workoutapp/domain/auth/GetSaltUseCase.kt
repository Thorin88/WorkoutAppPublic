package com.example.workoutapp.domain.auth

import android.util.Log
import com.example.workoutapp.models.workoutapi.Salt
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import javax.inject.Inject

/**
 * Makes a request to the Repository to obtain the user's salt value. This usecase is almost not
 * required, but it can make the code in the ViewModel more business oriented by converting the
 * repository response object into a set of more relevant fields for the ViewModel.
 *
 * The usecase encapsulates the null check on the salt value obtained.
 *
 * Not really needed, as only going to be used by the LoginUseCase, but done as an example.
 */
class GetSaltUseCase @Inject constructor (
    private val appointmentsAPIRepository: WorkoutAPIRepository,
) {

    suspend operator fun invoke(
        username: String,
    ): GetSaltResult {

        val response = appointmentsAPIRepository.getSalt(
            username = username,
        )

        Log.d("getSaltUseCase", "Response: $response")

        // Error handling here, as opposed to the ViewModel
        // Doing this here instead of result class to keep the result
        // class not concerned with how to process the result.

        when (response) {

            is WorkoutAPIRepositoryResponse.Success -> {

                val salt = response.data
                return if (salt == null) {
                    GetSaltResult.Error
                } else {
                    GetSaltResult.Success(salt = salt)
                }

            }

            is WorkoutAPIRepositoryResponse.Error -> {

                return if (response.code == 404) {
                    GetSaltResult.NoSuchUser
                } else {
                    GetSaltResult.Error
                }

            }

            else -> {
                Log.d("getSaltUsecase", "Unhandled/ignored repository response")
                return GetSaltResult.Error
            }

        }

    }

}

/**
 * Almost unneeded, but does also encapsulate the need to check the salt is not null.
 * Can be extended to contain information about more specific errors. In this case, it also handles
 * the case where the salt was null, so the ViewModel can just grab the salt if everything was fine.
 */
sealed class GetSaltResult {
    data class Success(val salt: Salt) : GetSaltResult()

    object NoSuchUser : GetSaltResult()

    object Error : GetSaltResult() // Could have a message, but at the moment the ViewModel doesn't need to know this specifically,
    // and this may differ based on the context this usecase is used.
}