package com.example.workoutapp.domain.loginfields

import javax.inject.Inject

/**
 * For convenience. Handles both username and password handling in one call. Executes the provided
 * functions depending on the validity of the provided field values.
 */
class HandleLoginFieldsUseCase @Inject constructor (
    private val handleUsernameFieldUseCase: HandleUsernameFieldUseCase,
    private val handlePasswordFieldUseCase: HandlePasswordFieldUseCase,
) {

    /**
     * Executes the provided functions depending on the validity of the login fields provided.
     */
    operator fun invoke(
        username : String,
        password : String,
        onUsernameInvalid : (String) -> Unit,
        onUsernameValid : () -> Unit,
        onPasswordInvalid : (String) -> Unit,
        onPasswordValid : () -> Unit,
    ) {

        handleUsernameFieldUseCase(
            username = username,
            onUsernameInvalid = onUsernameInvalid,
            onUsernameValid = onUsernameValid,
        )

        handlePasswordFieldUseCase(
            password = password,
            onPasswordInvalid = onPasswordInvalid,
            onPasswordValid = onPasswordValid,
        )

    }

}