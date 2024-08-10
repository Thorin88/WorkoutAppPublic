package com.example.workoutapp.domain.auth

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class HashPasswordUseCase @Inject constructor () {

    operator fun invoke(
        password: String,
        salt: ByteArray,
    ): Pair<String, String> {
        return hashPassword(
            password = password,
            salt = salt,
        )
    }

    operator fun invoke(
        password: String,
        salt: String,
    ): Pair<String, String> {
        return hashPassword(
            password = password,
            salt = salt,
        )
    }

    private fun hashPassword(password: String, salt: ByteArray): Pair<String, String> {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            // Append the salt to the password before hashing
            // IMPORTANT: Password is not a hex string, so calling toByteArray is sufficient
            val saltedPassword = password.toByteArray() + salt
            val hashBytes = digest.digest(saltedPassword)
            // Convert bytes to hex string. A regular string is not used as the string can contain the
            // null character, which is commonly used to indicate the end of strings.
            Pair(
                byteArrayToHexString(hashBytes),
                byteArrayToHexString(salt),
            )
        } catch (e: NoSuchAlgorithmException) {
            // TODO -> Handle exception, like throwing a more generic exception.
            throw e
        }
    }

    private fun hashPassword(password: String, salt: String): Pair<String, String> {
        val saltBytes = hexStringToByteArray(salt)
        return hashPassword(password, saltBytes)
    }

    private fun byteArrayToHexString(byteArray: ByteArray): String {
        return byteArray.joinToString("") { "%02x".format(it) }
    }

    // https://www.baeldung.com/kotlin/byte-arrays-to-hex-strings for info on how this works
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val result = ByteArray(hexString.length / 2)
        for (i in hexString.indices step 2) {
            result[i / 2] = hexString.substring(i, i + 2).toInt(16).toByte()
        }
        return result
    }

}