package com.mohamedfaridelsherbini.screenblock.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mohamedfaridelsherbini.screenblock.R
import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionEntity
import com.mohamedfaridelsherbini.screenblock.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onStartSession: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val recentSessions by viewModel.recentSessions.collectAsState()
    val todayMinutes by viewModel.totalFocusMinutesToday.collectAsState()
    val strictModeEnabled by viewModel.strictModeEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.AppSelection.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings_desc))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Daily Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.todays_focus), style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = stringResource(R.string.min_suffix, todayMinutes),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Strict Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.strict_mode_title), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.strict_mode_desc), style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = strictModeEnabled,
                    onCheckedChange = { viewModel.toggleStrictMode(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.start_a_session), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DurationButton(modifier = Modifier.weight(1f), minutes = 25) { onStartSession(25) }
                DurationButton(modifier = Modifier.weight(1f), minutes = 50) { onStartSession(50) }
                DurationButton(modifier = Modifier.weight(1f), minutes = 90) { onStartSession(90) }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedButton(
                onClick = { navController.navigate(Screen.EmergencyContacts.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.manage_emergency_contacts))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(stringResource(R.string.history), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(recentSessions) { session ->
                    SessionHistoryItem(session)
                }
            }
        }
    }
}

@Composable
fun DurationButton(modifier: Modifier = Modifier, minutes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = minutes.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "min", fontSize = 10.sp)
        }
    }
}

@Composable
fun SessionHistoryItem(session: FocusSessionEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.min_focus_preset, session.plannedDurationMinutes), 
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(session.status.name, style = MaterialTheme.typography.bodySmall)
            }
            if (session.blockedAppAttempts > 0) {
                Badge { Text(stringResource(R.string.app_blocks) + ": ${session.blockedAppAttempts}") }
            }
        }
    }
}
