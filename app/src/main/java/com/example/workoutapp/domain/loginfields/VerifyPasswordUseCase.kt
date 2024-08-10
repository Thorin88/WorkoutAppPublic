package com.example.workoutapp.domain.loginfields

import javax.inject.Inject

/**
 *
 */
class VerifyPasswordUseCase @Inject constructor () {

    operator fun invoke(
        password: String,
    ): VerifyPasswordResult {

        if (password.length <= 0) {
            return VerifyPasswordResult.Invalid(reason = "Password cannot be empty")
        }

        // TODO -> The UI can remove certain unwanted characters, like whitespace

        return VerifyPasswordResult.Valid

    }

}

/**
 *
 */
sealed class VerifyPasswordResult {
    object Valid : VerifyPasswordResult()
    // Just a string, as the UI will just indicate an error, it knows that field this relates too, and
    // can display a common message if desired, or use it's own. A password verification field may make
    // use of more specific things, eg indicating if a list of criteria are met to allow a checkbox system.
    data class Invalid(val reason: String): VerifyPasswordResult()
}