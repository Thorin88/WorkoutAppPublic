package com.example.workoutapp.sessionmanagers

import android.util.Log
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import com.example.workoutapp.room.databases.ExperimentalDatabase
import com.example.workoutapp.room.tables.TokenEntity
import javax.inject.Inject
import javax.inject.Provider

// TODO -> Encrypt tokens

class TokenManager @Inject constructor (
    // TODO -> Use a more generic type here, instead of the specific instance type
    private val localDatabase : ExperimentalDatabase,
    // Allows circular dependencies, by delaying creation of this instance, so this object
    // doesn't need to be created on instantiation of this object.
    private val appointmentsAPIRepositoryProvider: Provider<WorkoutAPIRepository>,
) {

    init {
        Log.d("TokenManager", "I was initialised")
    }

    private var cachedTokens : TokenEntity? = null

    suspend fun getNewAccessToken(): String? {

        val refreshToken = getCurrentRefreshToken()

        // Log.d("TokenManager.getNewAccessToken", "Making API call")

        val response = appointmentsAPIRepositoryProvider.get().getNewAccessToken(refreshToken = getTokenFromDb()?.refreshToken.toString())

        // Log.d("TokenManager.getNewAccessToken", "Made API call")

        when (response) {
            is WorkoutAPIRepositoryResponse.Success -> {

                val accessToken = response.data?.access_token

                Log.d("TokenManager", "Successfully generated a new access token: $accessToken")
                saveTokenToDb(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                )

                return accessToken

            }

            // Bit more tricky. For now, if retrieving this token goes wrong, just ask the user to log back in.
            // Not the perfect situation, but more than sufficient for now.
            else -> {
                Log.d("TokenManager", "Unable to generate a new access token")
                return null
            }

        }

    }

    suspend fun getCurrentAccessToken(): String? {

        val cachedAccessToken = cachedTokens?.accessToken
        if (cachedAccessToken != null) {
            Log.d("getCurrentAccessToken", "Returning cached access token")
            return cachedAccessToken
        }
        else {
            Log.d("getCurrentAccessToken", "Couldn't find cached token, trying database")
            return getTokenFromDb()?.accessToken
        }

    }

    suspend fun getCurrentRefreshToken(): String? {

        val cachedRefreshToken = cachedTokens?.refreshToken
        if (cachedRefreshToken != null) {
            Log.d("getCurrentRefreshToken", "Returning cached refresh token")
            return cachedRefreshToken
        }
        else {
            Log.d("getCurrentRefreshToken", "Couldn't find cached token, trying database")
            return getTokenFromDb()?.refreshToken
        }

    }

    suspend fun saveTokenToDb(accessToken: String?, refreshToken: String?) {
        val tokenEntity = TokenEntity(accessToken = accessToken, refreshToken = refreshToken)
        localDatabase.tokenDao().saveToken(tokenEntity)
        // Save locally too
        cachedTokens = tokenEntity
    }

    suspend fun getTokenFromDb(): TokenEntity? {
        return localDatabase.tokenDao().getToken()
    }

    suspend fun clearTokens(): Boolean {

        cachedTokens = null
        val numRowsDeleted = localDatabase.tokenDao().deleteToken()

        return numRowsDeleted == 1

    }

}