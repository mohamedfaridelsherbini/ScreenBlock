package com.mohamedfaridelsherbini.screenblock.domain

import android.content.Context
import android.util.Log
import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import com.mohamedfaridelsherbini.screenblock.domain.blocking.BlockingRule
import com.mohamedfaridelsherbini.screenblock.domain.blocking.rules.AllowedAppsRule
import com.mohamedfaridelsherbini.screenblock.domain.blocking.rules.AssistantRule
import com.mohamedfaridelsherbini.screenblock.domain.blocking.rules.LauncherRule
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBlockingEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val focusSessionManager: FocusSessionManager,
    private val preferenceManager: PreferenceManager,
    private val assistantRule: AssistantRule,
    private val launcherRule: LauncherRule,
    private val allowedAppsRule: AllowedAppsRule
) {
    suspend fun shouldBlock(packageName: String): Boolean {
        val session = focusSessionManager.currentSession.value
        if (session == null || session.status != FocusSessionStatus.ACTIVE) {
            return false
        }

        // System-level overrides (can't be blocked for safety/usability)
        if (packageName == context.packageName || packageName == "com.android.systemui") {
            return false
        }

        val strictMode = preferenceManager.strictModeEnabled.first()

        // 1. Check Assistant (Always blocked in focus if it's not our app)
        if (assistantRule.shouldBlock(packageName)) {
            Log.d("AppBlockingEngine", "Blocking Assistant: $packageName")
            return true
        }

        // 2. Check Launcher (Blocked only in Strict Mode)
        if (strictMode && launcherRule.shouldBlock(packageName)) {
            Log.d("AppBlockingEngine", "Blocking Launcher: $packageName")
            return true
        }

        // 3. Check User's Allowed Apps
        val shouldBlock = allowedAppsRule.shouldBlock(packageName)
        if (shouldBlock) {
            Log.d("AppBlockingEngine", "Blocking Distracting App: $packageName")
        }

        return shouldBlock
    }
}
