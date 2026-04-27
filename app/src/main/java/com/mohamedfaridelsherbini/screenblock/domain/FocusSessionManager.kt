package com.mohamedfaridelsherbini.screenblock.domain

import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionDao
import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionEntity
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSession
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusSessionManager @Inject constructor(
    private val focusSessionDao: FocusSessionDao
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var timerJob: Job? = null

    private val _currentSession = MutableStateFlow<FocusSession?>(null)
    val currentSession: StateFlow<FocusSession?> = _currentSession.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    fun startSession(durationMinutes: Int) {
        val session = FocusSession(
            id = UUID.randomUUID().toString(),
            startTimeMillis = System.currentTimeMillis(),
            plannedDurationMinutes = durationMinutes,
            status = FocusSessionStatus.ACTIVE
        )
        _currentSession.value = session
        startTimer(durationMinutes * 60L)
    }

    private fun startTimer(seconds: Long) {
        timerJob?.cancel()
        _remainingSeconds.value = seconds
        timerJob = scope.launch {
            while (_remainingSeconds.value > 0) {
                delay(1000)
                _remainingSeconds.value -= 1
            }
            endSession(FocusSessionStatus.COMPLETED)
        }
    }

    fun endSession(status: FocusSessionStatus) {
        timerJob?.cancel()
        timerJob = null

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

    private fun FocusSession.toEntity() = FocusSessionEntity(
        id = id,
        startTimeMillis = startTimeMillis,
        endTimeMillis = endTimeMillis,
        plannedDurationMinutes = plannedDurationMinutes,
        status = status,
        blockedAppAttempts = blockedAppAttempts,
        blockedNotifications = blockedNotifications
    )
}
