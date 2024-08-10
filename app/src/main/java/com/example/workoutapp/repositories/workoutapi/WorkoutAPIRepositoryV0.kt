package com.example.workoutapp.repositories.workoutapi

import android.util.Log
import com.example.workoutapp.domain.auth.GenerateSaltUseCase
import com.example.workoutapp.domain.auth.HashPasswordUseCase
import com.example.workoutapp.models.workoutapi.AccessToken
import com.example.workoutapp.models.workoutapi.Login
import com.example.workoutapp.models.workoutapi.RefreshToken
import com.example.workoutapp.models.workoutapi.Salt
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.SignUp
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutAPIBadStatusResponse
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.models.workoutapi.WorkoutComponentWithoutID
import com.example.workoutapp.models.workoutapi.WorkoutRecommendation
import com.example.workoutapp.models.workoutapi.WorkoutRecommendationRequest
import com.example.workoutapp.room.databases.ExperimentalDatabase
import com.example.workoutapp.sessionmanagers.TokenManager
import retrofit2.Response
import java.util.concurrent.CancellationException
import javax.inject.Provider

/**
 * Calls the endpoints defined in the Retrofit constructed API. Returns a fixed set of possible objects,
 * whose type can be checked by callers (usually ViewModels) in order to see the status of the API requests,
 * and define logic to extract values from the different objects that can be returned.
 */
class WorkoutAPIRepositoryV0(
    private val workoutAppAPIService : WorkoutAPI,
    private val localDatabase : ExperimentalDatabase, // Currently unused, but may be used for syncing in the further
    private val tokenManager: Provider<TokenManager>,
    private val generateSaltUseCase: GenerateSaltUseCase,
    private val hashPasswordUseCase: HashPasswordUseCase,
) : WorkoutAPIRepository {

    init {
        Log.d("WorkoutAPIRepositoryV0", "I was initialised")
    }

    // Only cached in the TokenManager, to ensure there is a single source of truth
    // private var cachedTokens : TokenEntity? = null

    // Takes requests from the VM and converts them into API calls, then returning
    // the data to the VM, who will format it for the V.

    // Using password here as the code in this function will be generating the hash. This generation
    // is therefore also done off the UI thread, so UI is more reactive.
    override suspend fun signUp(username: String, password: String): WorkoutAPIRepositoryResponse<String> {

        val (hash, salt) = hashPasswordUseCase(
            password = password,
            salt = generateSaltUseCase(),
        )

        val body = SignUp(
            username = username,
            hash = hash,
            salt = salt,
        )

        try {
            val response = workoutAppAPIService.signUp(body)

            if (!response.isSuccessful) {
                Log.d("signUp","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {
                val responseBody = response.body()
                val responseMessage = responseBody?.message.toString()
                Log.d("signUp","[${response.code()}] Request returned good status code")
                return WorkoutAPIRepositoryResponse.Success(
                    data = responseMessage,
                )
            }

        } catch (t: Throwable) {
            Log.d("signUp","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="signUp")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )

        }

    }

    override suspend fun getSalt(username: String): WorkoutAPIRepositoryResponse<Salt?> {

        try {
            val response = workoutAppAPIService.getSalt(username = username)

            if (!response.isSuccessful) {
                Log.d("getSalt","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {
                val responseBody = response.body()
                val payload = responseBody?.payload
                Log.d("getSalt","[${response.code()}] Request returned good status code")
                return WorkoutAPIRepositoryResponse.Success(
                    data = payload,
                )
            }

        } catch (t: Throwable) {
            Log.d("getSalt","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="getSalt")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

    // Does not guarantee that a refresh token is stored in the local database, but the surrounding
    // code will always be made to login again if login call does fail to add a token to the local database.
    override suspend fun login(username: String, hash: String): WorkoutAPIRepositoryResponse<RefreshToken?> {

        val body = Login(
            username = username,
            hash = hash,
        )

        try {
            val response = workoutAppAPIService.login(body)

            if (!response.isSuccessful) {
                Log.d("login","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {
                val responseBody = response.body()
                val payload = responseBody?.payload
                Log.d("login","[${response.code()}] Request returned good status code")

                Log.d("login","Returned payload: $payload")

                // Unique to login()
                // Store the new access token (in local database), if able to

                // Safe, but does not guarantee a token is saved. This is fine, it will just result in
                // the user being logged out in the rare case that the request was successful but the payload
                // was null.
                if (payload != null) {
                    tokenManager.get().saveTokenToDb(
                        accessToken = null,
                        refreshToken = payload.refresh_token,
                    )
                    Log.d("login","Saved refresh token to the local database")
                }

                // The less desirable alternative, allows null exceptions to occur in order to make sure we don't carry on
                // without this token.
                // saveTokenToDb(
                //     accessToken = null,
                //     refreshToken = payload!!.refresh_token,
                // )

                return WorkoutAPIRepositoryResponse.Success(
                    data = payload,
                )
            }

        } catch (t: Throwable) {
            Log.d("login","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="login")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

    override suspend fun getNewAccessToken(refreshToken: String): WorkoutAPIRepositoryResponse<AccessToken?> {

        try {
            val response = workoutAppAPIService.getNewAccessToken(refreshToken = refreshToken)

            if (!response.isSuccessful) {
                Log.d("getNewAccessToken","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {
                val responseBody = response.body()
                val payload = responseBody?.payload
                Log.d("getNewAccessToken","[${response.code()}] Request returned good status code")
                return WorkoutAPIRepositoryResponse.Success(
                    data = payload,
                )
            }

        } catch (t: Throwable) {
            Log.d("getNewAccessToken","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="getNewAccessToken")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

    private fun escalateCancellationException(
        t: Throwable,
        tag: String = "escalateCancellationException",
    ) {

        if (t is CancellationException) {
            Log.d(tag,"Propagating CancellationException...")
            throw t
        }

    }

    private inline fun <reified T> createErrorResponse(response: Response<T>) : WorkoutAPIRepositoryResponse<Nothing> {

        // Keeping 401 status codes separate from errors
        if (response.code() == 401) {
            return WorkoutAPIRepositoryResponse.Unauthenticated
        }

        val errorResponseBody = WorkoutAPIBadStatusResponse.getErrorBody(
            response = response,
        )
        val responseMessage = errorResponseBody?.message.toString()
        return WorkoutAPIRepositoryResponse.Error(
            code = response.code(),
            message = responseMessage,
        )
    }

    override suspend fun logout() : Boolean {

        val wasSuccessful = tokenManager.get().clearTokens()
        return wasSuccessful

    }

    override suspend fun createWorkout(
        workout: Workout<WorkoutComponentWithoutID>,
    ): WorkoutAPIRepositoryResponse<String> {

        val body = workout

        try {

            val response = workoutAppAPIService.createWorkout(body)

            if (!response.isSuccessful) {
                Log.d("createWorkout","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {

                val responseBody = response.body()
                val message = responseBody?.message
                Log.d("createWorkout","[${response.code()}] Request returned good status code")

                Log.d("createWorkout","Returned message: $message")

                return WorkoutAPIRepositoryResponse.Success(
                    data = message.toString(),
                )
            }

        } catch (t: Throwable) {
            Log.d("createWorkout","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="createWorkout")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

    override suspend fun requestWorkoutRecommendation(userQuery: String): WorkoutAPIRepositoryResponse<WorkoutRecommendation?> {
        try {
            val response = workoutAppAPIService.requestWorkoutRecommendation(
                data = WorkoutRecommendationRequest(
                    recommendation_request = userQuery,
                ),
            )

            if (!response.isSuccessful) {
                Log.d("requestWorkoutRecommendation","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {
                val responseBody = response.body()
                val payload = responseBody?.payload
                Log.d("requestWorkoutRecommendation","[${response.code()}] Request returned good status code")
                return WorkoutAPIRepositoryResponse.Success(
                    data = payload,
                )
            }

        } catch (t: Throwable) {
            Log.d("requestWorkoutRecommendation","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="requestWorkoutRecommendation")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }
    }

    override suspend fun getUserWorkouts(): WorkoutAPIRepositoryResponse<SavedWorkouts?> {

        try {
            val response = workoutAppAPIService.getUserWorkouts()

            if (!response.isSuccessful) {
                Log.d("getUserWorkouts","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {
                val responseBody = response.body()
                val payload = responseBody?.payload
                Log.d("getUserWorkouts","[${response.code()}] Request returned good status code")
                return WorkoutAPIRepositoryResponse.Success(
                    data = payload,
                )
            }

        } catch (t: Throwable) {
            Log.d("getUserWorkouts","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="getUserWorkouts")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

    override suspend fun updateWorkoutComponents (
        workoutComponents : List<WorkoutComponent>,
    ): WorkoutAPIRepositoryResponse<String> {

        val body = workoutComponents

        try {

            val response = workoutAppAPIService.updateWorkoutComponents(body)

            if (!response.isSuccessful) {
                Log.d("updateWorkoutComponents","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {

                val responseBody = response.body()
                val message = responseBody?.message
                Log.d("updateWorkoutComponents","[${response.code()}] Request returned good status code")

                Log.d("updateWorkoutComponents","Returned message: $message")

                return WorkoutAPIRepositoryResponse.Success(
                    data = message.toString(),
                )
            }

        } catch (t: Throwable) {
            Log.d("updateWorkoutComponents","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="updateWorkoutComponents")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

    override suspend fun finishWorkout (
        workoutComponents : List<WorkoutComponent>,
    ): WorkoutAPIRepositoryResponse<String> {

        val body = workoutComponents

        try {

            val response = workoutAppAPIService.finishWorkout(body)

            if (!response.isSuccessful) {
                Log.d("finishWorkout","[${response.code()}] Request returned bad status code")
                return createErrorResponse(response)
            }
            else {

                val responseBody = response.body()
                val message = responseBody?.message
                Log.d("finishWorkout","[${response.code()}] Request returned good status code")

                Log.d("finishWorkout","Returned message: $message")

                return WorkoutAPIRepositoryResponse.Success(
                    data = message.toString(),
                )
            }

        } catch (t: Throwable) {
            Log.d("finishWorkout","Request failed")
            val errorMessage = t.message
            println("Request failed: $errorMessage")
            t.printStackTrace()

            escalateCancellationException(t, tag="finishWorkout")

            return WorkoutAPIRepositoryResponse.Error(
                code = 500,
                message = errorMessage,
            )
        }

    }

}