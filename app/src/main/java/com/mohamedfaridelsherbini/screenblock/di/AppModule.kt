package com.mohamedfaridelsherbini.screenblock.di

import android.content.Context
import androidx.room.Room
import com.mohamedfaridelsherbini.screenblock.data.local.AppDatabase
import com.mohamedfaridelsherbini.screenblock.data.local.EmergencyContactDao
import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "focus_shield_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFocusSessionDao(database: AppDatabase): FocusSessionDao {
        return database.focusSessionDao()
    }

    @Provides
    fun provideEmergencyContactDao(database: AppDatabase): EmergencyContactDao {
        return database.emergencyContactDao()
    }
}
