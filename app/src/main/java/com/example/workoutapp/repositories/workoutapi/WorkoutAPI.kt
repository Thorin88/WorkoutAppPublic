package com.example.workoutapp.repositories.workoutapi

import com.example.workoutapp.models.workoutapi.AccessToken
import com.example.workoutapp.models.workoutapi.Login
import com.example.workoutapp.models.workoutapi.RefreshToken
import com.example.workoutapp.models.workoutapi.Salt
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.SignUp
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutAPIGETResponse
import com.example.workoutapp.models.workoutapi.WorkoutAPIResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.models.workoutapi.WorkoutComponentWithoutID
import com.example.workoutapp.models.workoutapi.WorkoutRecommendation
import com.example.workoutapp.models.workoutapi.WorkoutRecommendationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * A Retrofit interface representing the API implemented here: https://github.com/Thorin88/appointment-app-api
 */
interface WorkoutAPI {

    // https://stackoverflow.com/questions/58429501/unable-to-invoke-no-args-constructor-for-retrofit2-call
    // https://medium.com/android-news/kotlin-coroutines-and-retrofit-e0702d0b8e8f

    // TODO -> Endpoint which filters the results by users: https://guides.codepath.com/android/consuming-apis-with-retrofit
    // Will eventually be implemented via JWT token contents

    @POST("users/signup")
    suspend fun signUp(@Body data: SignUp): Response<WorkoutAPIResponse>

    @GET("users/salt")
    suspend fun getSalt(@Query("username") username: String): Response<WorkoutAPIGETResponse<Salt>>

    // Changed to returning a refresh token on success
    @POST("users/login")
    suspend fun login(@Body data: Login): Response<WorkoutAPIGETResponse<RefreshToken>>

    @GET("access_tokens")
    suspend fun getNewAccessToken(@Query("refresh_token") refreshToken: String): Response<WorkoutAPIGETResponse<AccessToken>>

    @POST("workouts/create")
    suspend fun createWorkout(@Body data: Workout<WorkoutComponentWithoutID>): Response<WorkoutAPIResponse>

    @POST("workouts/recommendation")
    suspend fun requestWorkoutRecommendation(@Body data: WorkoutRecommendationRequest): Response<WorkoutAPIGETResponse<WorkoutRecommendation>>

    // Refresh token/user id is basically a query parameter, but this is managed by the API looking in the provided access token
    // when API endpoints require authentication (defined as endpoint returning 401's).
    @GET("workouts/saved")
    suspend fun getUserWorkouts(): Response<WorkoutAPIGETResponse<SavedWorkouts>>

    @POST("workouts/update/components")
    suspend fun updateWorkoutComponents(@Body data: List<WorkoutComponent>): Response<WorkoutAPIResponse>

    @POST("workouts/finish")
    suspend fun finishWorkout(@Body data: List<WorkoutComponent>): Response<WorkoutAPIResponse>

}