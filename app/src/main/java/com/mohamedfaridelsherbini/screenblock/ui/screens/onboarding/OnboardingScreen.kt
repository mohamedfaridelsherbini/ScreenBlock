package com.mohamedfaridelsherbini.screenblock.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohamedfaridelsherbini.screenblock.R
import com.mohamedfaridelsherbini.screenblock.core.permissions.PermissionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    permissionManager: PermissionManager,
    onComplete: () -> Unit
) {
    var accessibilityEnabled by remember { mutableStateOf(permissionManager.isAccessibilityServiceEnabled()) }
    var notificationEnabled by remember { mutableStateOf(permissionManager.isNotificationListenerEnabled()) }
    var batteryOptimizationIgnored by remember { mutableStateOf(permissionManager.isIgnoringBatteryOptimizations()) }

    // Update state when returning to screen
    LaunchedEffect(Unit) {
        while(true) {
            accessibilityEnabled = permissionManager.isAccessibilityServiceEnabled()
            notificationEnabled = permissionManager.isNotificationListenerEnabled()
            batteryOptimizationIgnored = permissionManager.isIgnoringBatteryOptimizations()
            kotlinx.coroutines.delay(1000)
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_to_screenblock), 
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            PermissionRow(
                title = stringResource(R.string.accessibility_service_title),
                description = stringResource(R.string.accessibility_service_desc),
                isGranted = accessibilityEnabled,
                onClick = { permissionManager.openAccessibilitySettings() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PermissionRow(
                title = stringResource(R.string.notification_access_title),
                description = stringResource(R.string.notification_access_desc),
                isGranted = notificationEnabled,
                onClick = { permissionManager.openNotificationListenerSettings() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            PermissionRow(
                title = stringResource(R.string.battery_optimization_title),
                description = stringResource(R.string.battery_optimization_desc),
                isGranted = batteryOptimizationIgnored,
                onClick = { permissionManager.requestIgnoreBatteryOptimizations() }
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = { onComplete() },
                enabled = accessibilityEnabled && notificationEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.get_started))
            }
        }
    }
}

@Composable
fun PermissionRow(
    title: String,
    description: String,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodySmall)
            }
            if (isGranted) {
                Text("✅", style = MaterialTheme.typography.headlineSmall)
            } else {
                Text("❌", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
