package com.mohamedfaridelsherbini.screenblock.domain

import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionDao
import com.mohamedfaridelsherbini.screenblock.data.mapper.toEntity
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSession
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusSessionManager @Inject constructor(
    private val focusSessionDao: FocusSessionDao,
    private val focusTimer: FocusTimer
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentSession = MutableStateFlow<FocusSession?>(null)
    val currentSession: StateFlow<FocusSession?> = _currentSession.asStateFlow()

    val remainingSeconds: StateFlow<Long> = focusTimer.remainingSeconds

    fun startSession(durationMinutes: Int) {
        val session = FocusSession(
            id = UUID.randomUUID().toString(),
            startTimeMillis = System.currentTimeMillis(),
            plannedDurationMinutes = durationMinutes,
            status = FocusSessionStatus.ACTIVE
        )
        _currentSession.value = session
        
        focusTimer.start(
            seconds = durationMinutes * 60L,
            onTick = { /* Can be used for specific updates if needed */ },
            onFinish = { endSession(FocusSessionStatus.COMPLETED) }
        )
    }

    fun endSession(status: FocusSessionStatus) {
        focusTimer.stop()
        
        val updatedSession = _currentSession.value?.copy(
            status = status,
            endTimeMillis = System.currentTimeMillis()
        )
        _currentSession.value = updatedSession
        
        updatedSession?.let { session ->
            scope.launch {
                focusSessionDao.insertSession(session.toEntity())
            }
        }
    }

    fun incrementBlockedAppAttempts() {
        _currentSession.value = _currentSession.value?.let {
            it.copy(blockedAppAttempts = it.blockedAppAttempts + 1)
        }
    }

    fun incrementBlockedNotifications() {
        _currentSession.value = _currentSession.value?.let {
            it.copy(blockedNotifications = it.blockedNotifications + 1)
        }
    }
}
