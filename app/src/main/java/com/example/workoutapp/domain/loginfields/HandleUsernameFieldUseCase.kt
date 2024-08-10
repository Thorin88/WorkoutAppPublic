package com.example.workoutapp.domain.loginfields

import javax.inject.Inject

/**
 * Executes the provided functions based on the validity of the username field provided.
 *
 * This usecase is mainly to reduce the repetition of the `when` block which is utilised.
 */
class HandleUsernameFieldUseCase @Inject constructor (
    private val verifyUsernameUseCase: VerifyUsernameUseCase,
) {

    /**
     * Executes the functions depending on the validity of the username field provided.
     */
    operator fun invoke(
        username : String,
        onUsernameInvalid : (String) -> Unit,
        onUsernameValid : () -> Unit,
    ) {

        val verificationResult = verifyUsernameUseCase(
            username = username,
        )

        when (verificationResult) {
            is VerifyUsernameResult.Invalid -> {
                onUsernameInvalid(verificationResult.reason)
            }
            is VerifyUsernameResult.Valid -> {
                onUsernameValid()
            }
        }

    }

}