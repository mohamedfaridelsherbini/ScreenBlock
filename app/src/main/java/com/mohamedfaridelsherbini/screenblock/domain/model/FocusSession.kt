package com.mohamedfaridelsherbini.screenblock.domain.model

data class FocusSession(
    val id: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long? = null,
    val plannedDurationMinutes: Int,
    val status: FocusSessionStatus = FocusSessionStatus.ACTIVE,
    val blockedAppAttempts: Int = 0,
    val blockedNotifications: Int = 0
)

enum class FocusSessionStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}
