package com.mohamedfaridelsherbini.screenblock.system

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.mohamedfaridelsherbini.screenblock.domain.AppBlockingEngine
import com.mohamedfaridelsherbini.screenblock.domain.FocusSessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ScreenBlockNotificationListener : NotificationListenerService() {

    @Inject
    lateinit var appBlockingEngine: AppBlockingEngine

    @Inject
    lateinit var focusSessionManager: FocusSessionManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val packageName = sbn?.packageName ?: return
        
        serviceScope.launch {
            if (appBlockingEngine.shouldBlock(packageName)) {
                cancelNotification(sbn.key)
                focusSessionManager.incrementBlockedNotifications()
            }
        }
    }
}
