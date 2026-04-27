package com.mohamedfaridelsherbini.screenblock

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohamedfaridelsherbini.screenblock.core.permissions.PermissionManager
import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import com.mohamedfaridelsherbini.screenblock.domain.FocusSessionManager
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import com.mohamedfaridelsherbini.screenblock.system.FocusForegroundService
import com.mohamedfaridelsherbini.screenblock.ui.navigation.Screen
import com.mohamedfaridelsherbini.screenblock.ui.screens.appselection.AppSelectionScreen
import com.mohamedfaridelsherbini.screenblock.ui.screens.emergency.EmergencyContactScreen
import com.mohamedfaridelsherbini.screenblock.ui.screens.focus.FocusScreen
import com.mohamedfaridelsherbini.screenblock.ui.screens.home.HomeScreen
import com.mohamedfaridelsherbini.screenblock.ui.screens.onboarding.OnboardingScreen
import com.mohamedfaridelsherbini.screenblock.ui.screens.summary.SummaryScreen
import com.mohamedfaridelsherbini.screenblock.ui.theme.ScreenBlockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var focusSessionManager: FocusSessionManager

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScreenBlockTheme {
                val controller = rememberNavController()
                navController = controller
                
                val currentSession by focusSessionManager.currentSession.collectAsState()
                val onboardingCompleted by preferenceManager.onboardingCompleted.collectAsState(initial = null)
                val scope = rememberCoroutineScope()

                if (onboardingCompleted == null) return@ScreenBlockTheme

                NavHost(
                    navController = controller,
                    startDestination = if (onboardingCompleted == true) Screen.Home.route else Screen.Onboarding.route
                ) {
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            navController = controller,
                            permissionManager = permissionManager,
                            onComplete = {
                                scope.launch {
                                    preferenceManager.setOnboardingCompleted(true)
                                }
                                controller.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Home.route) {
                        HomeScreen(
                            navController = controller,
                            onStartSession = { duration ->
                                focusSessionManager.startSession(duration)
                                startFocusService()
                            }
                        )
                    }
                    composable(Screen.AppSelection.route) {
                        AppSelectionScreen(navController = controller)
                    }
                    composable(Screen.EmergencyContacts.route) {
                        EmergencyContactScreen(navController = controller)
                    }
                    composable(Screen.Focus.route) {
                        FocusScreen(
                            focusSessionManager = focusSessionManager,
                            onEndSession = {
                                focusSessionManager.endSession(FocusSessionStatus.COMPLETED)
                                stopFocusService()
                                controller.navigate(Screen.Summary.route) {
                                    popUpTo(Screen.Focus.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Summary.route) {
                        currentSession?.let { session ->
                            SummaryScreen(
                                session = session,
                                navController = controller
                            )
                        } ?: run {
                            controller.navigate(Screen.Home.route)
                        }
                    }
                }

                // Force navigation to Focus screen if session is active
                LaunchedEffect(currentSession?.status) {
                    if (currentSession?.status == FocusSessionStatus.ACTIVE) {
                        controller.navigate(Screen.Focus.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Ensure we are showing the Focus screen if re-opened while active
        val currentSession = focusSessionManager.currentSession.value
        if (currentSession?.status == FocusSessionStatus.ACTIVE) {
            navController?.navigate(Screen.Focus.route) {
                launchSingleTop = true
            }
        }
    }

    private fun startFocusService() {
        val intent = Intent(this, FocusForegroundService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopFocusService() {
        val intent = Intent(this, FocusForegroundService::class.java)
        stopService(intent)
    }
}
