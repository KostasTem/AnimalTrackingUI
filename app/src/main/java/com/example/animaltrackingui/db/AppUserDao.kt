package com.example.animaltrackingui.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppUserDao {
    @Query("SELECT * from user LIMIT 1")
    suspend fun getUser(): AppUser?

    @Query("SELECT * from user LIMIT 1")
    fun getUserSync(): AppUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: AppUser)

    @Delete
    suspend fun delete(user: AppUser)

    @Update
    suspend fun update(user: AppUser)
}