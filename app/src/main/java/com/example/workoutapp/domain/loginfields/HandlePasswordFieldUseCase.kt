package com.example.workoutapp.domain.loginfields

import javax.inject.Inject

/**
 * Executes the provided functions based on the validity of the password field provided.
 *
 * This usecase is mainly to reduce the repetition of the `when` block which is utilised.
 */
class HandlePasswordFieldUseCase @Inject constructor (
    private val verifyPasswordUseCase: VerifyPasswordUseCase,
) {

    /**
     * Executes the provided functions depending on the validity of the username field provided.
     */
    operator fun invoke(
        password : String,
        onPasswordInvalid : (String) -> Unit,
        onPasswordValid : () -> Unit,
    ) {

        val verificationResult = verifyPasswordUseCase(
            password = password,
        )

        // TODO -> In future could return something to indicate multiple things being wrong, eg length, needing special characters, numbers, ect
        // TODO -> Eg a string or object for the UI to render that indicates process towards the password being complete.
        when (verificationResult) {
            is VerifyPasswordResult.Invalid -> {
                onPasswordInvalid(verificationResult.reason)
            }
            is VerifyPasswordResult.Valid -> {
                onPasswordValid()
            }
        }

    }

}