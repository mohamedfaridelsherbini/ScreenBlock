package com.mohamedfaridelsherbini.screenblock.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY startTimeMillis DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity)

    @Query("SELECT * FROM focus_sessions WHERE id = :id")
    suspend fun getSessionById(id: String): FocusSessionEntity?

    @Query("SELECT SUM(plannedDurationMinutes) FROM focus_sessions WHERE startTimeMillis >= :sinceMillis AND status = 'COMPLETED'")
    fun getTotalFocusMinutesSince(sinceMillis: Long): Flow<Int?>
}
