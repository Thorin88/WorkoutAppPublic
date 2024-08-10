package com.example.workoutapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.workoutapp.room.daos.TokenDao
import com.example.workoutapp.room.databases.ExperimentalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnDeviceDatabaseModule {

    // Note that we are able to access the application context here
    @Provides
    @Singleton
    fun provideTokenDatabase(@ApplicationContext context: Context): ExperimentalDatabase {

        Log.d("[DaggerHilt] provideTokenDatabase", "Created database instance")

        return Room.databaseBuilder(
            context,
            ExperimentalDatabase::class.java, // The more specific type of the database
            "experimental_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTokenDao(database: ExperimentalDatabase): TokenDao {

        Log.d("[DaggerHilt] provideTokenDao", "Created token dao")

        return database.tokenDao()
    }
}