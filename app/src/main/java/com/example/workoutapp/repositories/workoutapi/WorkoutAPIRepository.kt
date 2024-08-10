package com.example.workoutapp.repositories.workoutapi

import com.example.workoutapp.models.workoutapi.AccessToken
import com.example.workoutapp.models.workoutapi.RefreshToken
import com.example.workoutapp.models.workoutapi.Salt
import com.example.workoutapp.models.workoutapi.SavedWorkouts
import com.example.workoutapp.models.workoutapi.Workout
import com.example.workoutapp.models.workoutapi.WorkoutAPIRepositoryResponse
import com.example.workoutapp.models.workoutapi.WorkoutAPIResponse
import com.example.workoutapp.models.workoutapi.WorkoutComponent
import com.example.workoutapp.models.workoutapi.WorkoutComponentWithoutID
import com.example.workoutapp.models.workoutapi.WorkoutRecommendation
import retrofit2.Response
import retrofit2.http.Body

interface WorkoutAPIRepository {

    /**
     * Attempts to sign up using the fields provided
     */
    suspend fun signUp(username: String, password: String): WorkoutAPIRepositoryResponse<String>

    /**
     * Gets the salt value that was stored when this user first signed up, used for generating a
     * password hash whenever the user tries to login
     */
    suspend fun getSalt(username: String): WorkoutAPIRepositoryResponse<Salt?>

    /**
     * Attempts to login using the fields provided
     */
    suspend fun login(username: String, hash: String): WorkoutAPIRepositoryResponse<RefreshToken?>

    /**
     * Requests a new access token by providing a refresh token
     */
    suspend fun getNewAccessToken(refreshToken: String): WorkoutAPIRepositoryResponse<AccessToken?>

    suspend fun logout() : Boolean

    suspend fun createWorkout(workout: Workout<WorkoutComponentWithoutID>): WorkoutAPIRepositoryResponse<String>

    suspend fun requestWorkoutRecommendation(userQuery: String): WorkoutAPIRepositoryResponse<WorkoutRecommendation?>

    suspend fun getUserWorkouts(): WorkoutAPIRepositoryResponse<SavedWorkouts?>

    suspend fun updateWorkoutComponents(workoutComponents: List<WorkoutComponent>): WorkoutAPIRepositoryResponse<String>

    suspend fun finishWorkout(workoutComponents: List<WorkoutComponent>): WorkoutAPIRepositoryResponse<String>

}