package com.mohamedfaridelsherbini.screenblock.ui.screens.focus

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.mohamedfaridelsherbini.screenblock.R
import com.mohamedfaridelsherbini.screenblock.domain.FocusSessionManager
import com.mohamedfaridelsherbini.screenblock.domain.model.EmergencyContact
import com.mohamedfaridelsherbini.screenblock.ui.screens.appselection.AppInfo
import java.util.Locale

@Composable
fun FocusScreen(
    focusSessionManager: FocusSessionManager,
    onEndSession: () -> Unit,
    viewModel: FocusViewModel = hiltViewModel(),
) {
    val remainingSeconds by focusSessionManager.remainingSeconds.collectAsState()
    val emergencyContacts by viewModel.emergencyContacts.collectAsState()
    val allowedApps by viewModel.allowedApps.collectAsState()
    val context = LocalContext.current

    // This handles and consumes the back button press
    BackHandler(enabled = true) {
        // Do nothing, effectively blocking the back button
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.focus_active),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = timerText,
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.session_in_progress),
                color = Color.Gray,
                fontSize = 18.sp
            )

            if (allowedApps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = stringResource(R.string.allowed_apps),
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow {
                    items(allowedApps) { app ->
                        AllowedAppItem(app = app) {
                            val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                            intent?.let { context.startActivity(it) }
                        }
                    }
                }
            }
            
            if (emergencyContacts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.emergency_contacts),
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow {
                    items(emergencyContacts) { contact ->
                        EmergencyContactItem(contact = contact) {
                            val intent = Intent(Intent.ACTION_DIAL, "tel:${contact.phoneNumber}".toUri())
                            context.startActivity(intent)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = onEndSession,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(stringResource(R.string.end_session), color = Color.White)
            }
        }
    }
}

@Composable
fun AllowedAppItem(app: AppInfo, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = SolidColor(Color.White))
    ) {
        Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(app.appName)
    }
}

@Composable
fun EmergencyContactItem(contact: EmergencyContact, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = SolidColor(Color.White))
    ) {
        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(contact.displayName)
    }
}
