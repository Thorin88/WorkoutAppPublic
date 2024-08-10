package com.example.workoutapp.domain.auth

import java.security.SecureRandom
import javax.inject.Inject

class GenerateSaltUseCase @Inject constructor () {

    operator fun invoke(): ByteArray {
        return generateSalt()
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        val random = SecureRandom()
        random.nextBytes(salt)
        return salt
    }

}