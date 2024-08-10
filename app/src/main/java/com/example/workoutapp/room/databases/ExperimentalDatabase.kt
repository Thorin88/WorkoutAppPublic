package com.example.workoutapp.room.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workoutapp.room.daos.TokenDao
import com.example.workoutapp.room.tables.TokenEntity

// Doesn't have a V0, like with the Repository, as this is an abstract class that is implemented by
// the Room library at the time of building the database. To use a V0, one way could be to instead have the
// TokenDao be a type parameter that is passed to this class, so a test TokenDao can be used.
@Database(entities = [TokenEntity::class], version = 1)
abstract class ExperimentalDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}