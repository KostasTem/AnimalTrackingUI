package com.example.animaltrackingui.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface SettingDao {

    @Query("SELECT * from setting")
    suspend fun getSettings(): List<Setting>?

    @Query("SELECT * from setting WHERE name=:name")
    suspend fun getSetting(name:String): Setting?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(setting: Setting)

    @Update
    suspend fun update(setting: Setting)

    @Delete
    suspend fun delete(setting: Setting)

}