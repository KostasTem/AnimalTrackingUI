package com.example.animaltrackingui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.animaltrackingui.utils.Converters

@Database(entities = [AppUser::class, Device::class, Setting::class], version = 11)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): AppUserDao
    abstract fun deviceDao(): DeviceDao
    abstract fun settingDao(): SettingDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "atdb")
                    .fallbackToDestructiveMigration()
                    .enableMultiInstanceInvalidation()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}