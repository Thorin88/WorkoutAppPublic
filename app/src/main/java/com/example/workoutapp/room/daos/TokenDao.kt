package com.example.workoutapp.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutapp.room.tables.TokenEntity

@Dao
interface TokenDao {
    @Query("SELECT * FROM tokens WHERE id = 1")
    suspend fun getToken(): TokenEntity?

    // Could make an alternative that uses UPDATE, to avoid using the refreshToken
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(tokenEntity: TokenEntity)

    @Query("DELETE FROM tokens WHERE id = 1")
    suspend fun deleteToken(): Int

}