package com.example.workoutapp.domain.loginfields

import javax.inject.Inject

/**
 *
 */
class VerifyUsernameUseCase @Inject constructor () {

    operator fun invoke(
        username: String,
    ): VerifyUsernameResult {

        if (username.length <= 0) {
            return VerifyUsernameResult.Invalid(reason = "Username cannot be empty")
        }

        // TODO -> Length limit + reason
        // text = "Limit: ${text.length}/$charLimit",

        // TODO -> The UI can remove certain unwanted characters, like whitespace

        return VerifyUsernameResult.Valid

    }

}

/**
 *
 */
sealed class VerifyUsernameResult {
    object Valid : VerifyUsernameResult()
    // Just a string, as the UI will just indicate an error, it knows that field this relates too, and
    // can display a common message if desired, or use it's own. A password verification field may make
    // use of more specific things, eg indicating if a list of criteria are met to allow a checkbox system.
    data class Invalid(val reason: String): VerifyUsernameResult()
}