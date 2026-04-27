package com.mohamedfaridelsherbini.screenblock.ui.screens.focus

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import com.mohamedfaridelsherbini.screenblock.data.repository.EmergencyContactRepository
import com.mohamedfaridelsherbini.screenblock.domain.model.EmergencyContact
import com.mohamedfaridelsherbini.screenblock.ui.screens.appselection.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contactRepository: EmergencyContactRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val emergencyContacts: StateFlow<List<EmergencyContact>> = contactRepository.emergencyContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allowedApps: StateFlow<List<AppInfo>> = preferenceManager.allowedPackages.map { allowed ->
        val pm = context.packageManager
        allowed.mapNotNull { packageName ->
            try {
                val appInfo = pm.getApplicationInfo(packageName, 0)
                val appName = pm.getApplicationLabel(appInfo).toString()
                AppInfo(packageName, appName, true)
            } catch (e: Exception) {
                null
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
