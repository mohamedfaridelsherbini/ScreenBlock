package com.mohamedfaridelsherbini.screenblock.system

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.mohamedfaridelsherbini.screenblock.MainActivity
import com.mohamedfaridelsherbini.screenblock.domain.AppBlockingEngine
import com.mohamedfaridelsherbini.screenblock.domain.FocusSessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScreenBlockAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var appBlockingEngine: AppBlockingEngine

    @Inject
    lateinit var focusSessionManager: FocusSessionManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("AccessibilityService", "Service Connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return
        
        // Log all events for debugging
        Log.d("AccessibilityService", "Event from: $packageName, Type: ${AccessibilityEvent.eventTypeToString(event.eventType)}")
        
        serviceScope.launch {
            if (appBlockingEngine.shouldBlock(packageName)) {
                Log.d("AccessibilityService", "Redirecting from: $packageName")
                focusSessionManager.incrementBlockedAppAttempts()
                redirectToFocusScreen()
            }
        }
    }

    private fun redirectToFocusScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {
            // Force the app to the front and ensure it doesn't create multiple instances
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {}
}
