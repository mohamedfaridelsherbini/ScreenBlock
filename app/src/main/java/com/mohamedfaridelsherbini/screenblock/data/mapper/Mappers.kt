package com.mohamedfaridelsherbini.screenblock.data.mapper

import com.mohamedfaridelsherbini.screenblock.data.local.EmergencyContactEntity
import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionEntity
import com.mohamedfaridelsherbini.screenblock.domain.model.EmergencyContact
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSession

fun FocusSession.toEntity() = FocusSessionEntity(
    id = id,
    startTimeMillis = startTimeMillis,
    endTimeMillis = endTimeMillis,
    plannedDurationMinutes = plannedDurationMinutes,
    status = status,
    blockedAppAttempts = blockedAppAttempts,
    blockedNotifications = blockedNotifications
)

fun FocusSessionEntity.toDomain() = FocusSession(
    id = id,
    startTimeMillis = startTimeMillis,
    endTimeMillis = endTimeMillis,
    plannedDurationMinutes = plannedDurationMinutes,
    status = status,
    blockedAppAttempts = blockedAppAttempts,
    blockedNotifications = blockedNotifications
)

fun EmergencyContact.toEntity() = EmergencyContactEntity(
    id = id,
    displayName = displayName,
    phoneNumber = phoneNumber
)

fun EmergencyContactEntity.toDomain() = EmergencyContact(
    id = id,
    displayName = displayName,
    phoneNumber = phoneNumber
)
