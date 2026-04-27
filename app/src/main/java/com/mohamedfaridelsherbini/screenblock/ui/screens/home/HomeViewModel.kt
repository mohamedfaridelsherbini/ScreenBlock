package com.mohamedfaridelsherbini.screenblock.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionDao
import com.mohamedfaridelsherbini.screenblock.data.local.FocusSessionEntity
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val focusSessionDao: FocusSessionDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val recentSessions: StateFlow<List<FocusSessionEntity>> = focusSessionDao.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalFocusMinutesToday: StateFlow<Int> = recentSessions.map { sessions ->
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        sessions.filter { 
            it.startTimeMillis >= today && it.status == FocusSessionStatus.COMPLETED 
        }.sumOf { it.plannedDurationMinutes }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val strictModeEnabled: StateFlow<Boolean> = preferenceManager.strictModeEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun toggleStrictMode(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.setStrictModeEnabled(enabled)
        }
    }
}
