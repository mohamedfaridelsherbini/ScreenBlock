package com.mohamedfaridelsherbini.screenblock.domain

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBlockingEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val focusSessionManager: FocusSessionManager,
    private val preferenceManager: PreferenceManager
) {
    private val assistantPackages = setOf(
        "com.google.android.googlequicksearchbox",
        "com.google.android.apps.googleassistant",
        "com.samsung.android.bixby.agent",
        "com.microsoft.cortana"
    )

    suspend fun shouldBlock(packageName: String): Boolean {
        val session = focusSessionManager.currentSession.value
        if (session == null || session.status != FocusSessionStatus.ACTIVE) {
            return false
        }

        // Always allow our own app
        if (packageName == context.packageName) {
            return false
        }

        // Allow system UI (Status bar etc)
        if (packageName == "com.android.systemui") {
            return false 
        }

        val strictMode = preferenceManager.strictModeEnabled.first()

        // Block Assistant
        if (assistantPackages.any { packageName.contains(it) }) {
            Log.d("AppBlockingEngine", "Blocking Assistant: $packageName")
            return true
        }

        // Block other launchers in Strict Mode
        if (strictMode && isLauncherPackage(packageName)) {
            Log.d("AppBlockingEngine", "Blocking Launcher: $packageName")
            return true
        }

        val allowedPackages = preferenceManager.allowedPackages.first()
        val isAllowed = allowedPackages.contains(packageName)
        
        if (!isAllowed) {
            Log.d("AppBlockingEngine", "Blocking app: $packageName")
        }
        
        return !isAllowed
    }

    private fun isLauncherPackage(packageName: String): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfos = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return resolveInfos.any { it.activityInfo.packageName == packageName }
    }
}
