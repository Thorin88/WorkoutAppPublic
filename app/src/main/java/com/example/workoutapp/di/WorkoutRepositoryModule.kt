package com.example.workoutapp.di

import android.app.Application
import com.example.workoutapp.R
import com.example.workoutapp.domain.auth.GenerateSaltUseCase
import com.example.workoutapp.domain.auth.HashPasswordUseCase
import com.example.workoutapp.repositories.workoutapi.WorkoutAPI
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepositoryV0
import com.example.workoutapp.room.databases.ExperimentalDatabase
import com.example.workoutapp.sessionmanagers.TokenAuthenticator
import com.example.workoutapp.sessionmanagers.TokenInterceptor
import com.example.workoutapp.sessionmanagers.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkoutRepositoryModule {

    // After JWT tokens
    @Provides
    @Singleton
    fun provideAppointmentsAPI(
        daggerHiltTokenAuthenticator: TokenAuthenticator,
        daggerHiltTokenTokenInterceptor: TokenInterceptor, // May not need to be delayed
    ): WorkoutAPI {

        // Making an API Service
        val baseUrl : String = "http://10.0.2.2:8080/"

        val client = OkHttpClient.Builder()
            // ... other configurations
            .authenticator(daggerHiltTokenAuthenticator)
            .addInterceptor(daggerHiltTokenTokenInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl) // Replace with your API base URL
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WorkoutAPI::class.java)

    }

    // Way 1:
    @Provides
    @Singleton
    fun provideAppointmentsAPIRepository(
        daggerHiltWorkoutAPIInstance: WorkoutAPI,
        daggerHiltDatabaseInstance: ExperimentalDatabase,
        daggerHiltDelayedTokenManagerInstance: Provider<TokenManager>,
        daggerHiltGenerateSaltUseCase: GenerateSaltUseCase,
        daggerHiltHashPasswordUseCase: HashPasswordUseCase,
        app: Application,
    ): WorkoutAPIRepository {

        println("DaggerHilt Injector: App name is: ${app.getString(R.string.app_name)}")

        // Note the need for an instance of a dependency here, but DaggerHilt will look for this if we
        // provide the instance in the parameters of this provide function.
        return WorkoutAPIRepositoryV0(
            workoutAppAPIService = daggerHiltWorkoutAPIInstance,
            localDatabase = daggerHiltDatabaseInstance,
            tokenManager = daggerHiltDelayedTokenManagerInstance,
            generateSaltUseCase = daggerHiltGenerateSaltUseCase,
            hashPasswordUseCase = daggerHiltHashPasswordUseCase,
            // can this use pass the app context in here,
        )

    }

}