package com.example.workoutapp.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO -> Have this used by the API response object? Or is this coupling unnecessarily tight?
// Answer: Do not use for the API return types, using this object would complicate things
@Entity(tableName = "tokens")
data class TokenEntity(
    @PrimaryKey val id: Int = 1, // Allowing only one token entry to exist in the table, at least via creation of entities via this constructor
    var accessToken: String? = null,
    var refreshToken: String? = null, // Nullable, as the TokenEntity itself is nullable, so redundant trying to have this field non-null
)
