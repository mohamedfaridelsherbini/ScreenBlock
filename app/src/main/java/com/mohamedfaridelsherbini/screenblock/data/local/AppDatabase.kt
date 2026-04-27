package com.mohamedfaridelsherbini.screenblock.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FocusSessionEntity::class, EmergencyContactEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun emergencyContactDao(): EmergencyContactDao
}
