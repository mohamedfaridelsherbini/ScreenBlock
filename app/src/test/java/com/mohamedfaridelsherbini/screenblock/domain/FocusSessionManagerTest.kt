package com.mohamedfaridelsherbini.screenblock.domain

import app.cash.turbine.test
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FocusSessionManagerTest {

    private lateinit var focusSessionManager: FocusSessionManager

    @Before
    fun setUp() {
        focusSessionManager = FocusSessionManager()
    }

    @Test
    fun `initial session is null`() = runTest {
        assertNull(focusSessionManager.currentSession.value)
    }

    @Test
    fun `starting session updates state`() = runTest {
        focusSessionManager.startSession(25)
        
        val session = focusSessionManager.currentSession.value
        assertNotNull(session)
        assertEquals(25, session?.plannedDurationMinutes)
        assertEquals(FocusSessionStatus.ACTIVE, session?.status)
    }

    @Test
    fun `ending session updates status and end time`() = runTest {
        focusSessionManager.startSession(25)
        focusSessionManager.endSession(FocusSessionStatus.COMPLETED)
        
        val session = focusSessionManager.currentSession.value
        assertEquals(FocusSessionStatus.COMPLETED, session?.status)
        assertNotNull(session?.endTimeMillis)
    }

    @Test
    fun `incrementing blocked attempts updates session`() = runTest {
        focusSessionManager.startSession(25)
        focusSessionManager.incrementBlockedAppAttempts()
        
        assertEquals(1, focusSessionManager.currentSession.value?.blockedAppAttempts)
    }

    @Test
    fun `incrementing blocked notifications updates session`() = runTest {
        focusSessionManager.startSession(25)
        focusSessionManager.incrementBlockedNotifications()
        
        assertEquals(1, focusSessionManager.currentSession.value?.blockedNotifications)
    }
}
