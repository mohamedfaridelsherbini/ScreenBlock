package com.mohamedfaridelsherbini.screenblock.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey val id: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long?,
    val plannedDurationMinutes: Int,
    val status: FocusSessionStatus,
    val blockedAppAttempts: Int,
    val blockedNotifications: Int
)
