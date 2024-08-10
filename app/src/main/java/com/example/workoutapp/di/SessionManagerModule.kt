package com.example.workoutapp.di

import android.util.Log
import com.example.workoutapp.repositories.workoutapi.WorkoutAPIRepository
import com.example.workoutapp.room.databases.ExperimentalDatabase
import com.example.workoutapp.sessionmanagers.TokenAuthenticator
import com.example.workoutapp.sessionmanagers.TokenInterceptor
import com.example.workoutapp.sessionmanagers.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionManagerModule {

    // May have to move this if environment/run-type flags are introduced.
    private val AUTHORIZATION_HEADER = "Authorization"

    @Provides
    @Singleton
    fun provideTokenManager(
        daggerHiltDatabaseInstance: ExperimentalDatabase,
        daggerHiltDelayedAppointmentsAPIRepositoryInstance: Provider<WorkoutAPIRepository>,
    ): TokenManager {

        Log.d("[DaggerHilt] provideTokenManager", "Created TokenManager instance")

        return TokenManager(
            localDatabase = daggerHiltDatabaseInstance,
            appointmentsAPIRepositoryProvider = daggerHiltDelayedAppointmentsAPIRepositoryInstance,
        )
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        daggerHiltTokenManager: TokenManager,
    ): TokenAuthenticator {

        Log.d("[DaggerHilt] provideTokenAuthenticator", "Created TokenAuthenticator instance")

        return TokenAuthenticator(
            authorizationHeader = AUTHORIZATION_HEADER,
            tokenManager = daggerHiltTokenManager,
        )
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(
        daggerHiltTokenManager: TokenManager,
    ): TokenInterceptor {

        Log.d("[DaggerHilt] provideTokenInterceptor", "Created TokenInterceptor instance")

        return TokenInterceptor(
            authorizationHeader = AUTHORIZATION_HEADER,
            tokenManager = daggerHiltTokenManager,
        )
    }

}