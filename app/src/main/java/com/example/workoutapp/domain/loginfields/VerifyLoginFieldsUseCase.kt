package com.example.workoutapp.domain.loginfields

import javax.inject.Inject

/**
 * Combines the username and password validity checks into one call. This can be used where
 * the logic intends to check both fields instead of each field individually, such as when clicking
 * the login button.
 */
class VerifyLoginFieldsUseCase @Inject constructor (
    private val verifyUsernameUseCase: VerifyUsernameUseCase,
    private val verifyPasswordUseCase: VerifyPasswordUseCase,
) {

    /**
     * Returns a list of results, as there can be multiple things going wrong, and the callers need
     * to know if the previously incorrect fields were resolved or not.
     */
    operator fun invoke(
        username : String,
        password : String,
    ) : List<VerifyLoginFieldsResult> {

        var verificationResults = mutableListOf<VerifyLoginFieldsResult>()

        verificationResults.add(
            getUsernameResult(
                username = username,
            )
        )

        verificationResults.add(
            getPasswordResult(
                password = password,
            )
        )

        return verificationResults

    }

    private fun getUsernameResult(
        username : String,
    ) : VerifyLoginFieldsResult {

        val usernameCheckResult = verifyUsernameUseCase(
            username = username,
        )

        return when (usernameCheckResult) {
            is VerifyUsernameResult.Invalid -> {
                VerifyLoginFieldsResult.Invalid.InvalidUsername(
                    message = usernameCheckResult.reason,
                )
            }

            is VerifyUsernameResult.Valid -> {
                VerifyLoginFieldsResult.Valid.ValidUsername
            }
        }

    }

    private fun getPasswordResult(
        password: String,
    ) : VerifyLoginFieldsResult {

        val passwordCheckResult = verifyPasswordUseCase(
            password = password,
        )

        return when (passwordCheckResult) {
            is VerifyPasswordResult.Invalid -> {
                VerifyLoginFieldsResult.Invalid.InvalidPassword(
                    message = passwordCheckResult.reason
                )
            }

            is VerifyPasswordResult.Valid -> {
                VerifyLoginFieldsResult.Valid.ValidPassword
            }
        }

    }

}

sealed class VerifyLoginFieldsResult {
    // This needs to be ValidFieldName objects, to allow the callers to distinguish which successful
    // verification was what.
    sealed class Valid : VerifyLoginFieldsResult() {
        // Is there need for nesting here?
        object ValidUsername: Valid()
        object ValidPassword: Valid()
    }
    // Could be a sealed class that indicates the type of invalid credentials
    sealed class Invalid: VerifyLoginFieldsResult() {
        // Useful to the ViewModel where this error is coming from, so it can highlight the
        // correct UI elements, compared to just returning InvalidCredentials with a message.
        data class InvalidUsername(val message: String) : Invalid()
        data class InvalidPassword(val message: String): Invalid()
    }
}