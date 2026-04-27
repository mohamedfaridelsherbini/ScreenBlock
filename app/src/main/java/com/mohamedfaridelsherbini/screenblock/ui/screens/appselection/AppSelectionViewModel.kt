package com.mohamedfaridelsherbini.screenblock.ui.screens.appselection

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppInfo(
    val packageName: String,
    val appName: String,
    val isAllowed: Boolean
)

@HiltViewModel
class AppSelectionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val apps: StateFlow<List<AppInfo>> = combine(
        preferenceManager.allowedPackages,
        _searchQuery
    ) { allowed, query ->
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfos = pm.queryIntentActivities(intent, 0)

        resolveInfos.map {
            val packageName = it.activityInfo.packageName
            val appName = it.loadLabel(pm).toString()
            AppInfo(packageName, appName, allowed.contains(packageName))
        }.filter {
            it.appName.contains(query, ignoreCase = true)
        }.sortedBy { it.appName }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleApp(packageName: String) {
        viewModelScope.launch {
            val currentAllowed = preferenceManager.allowedPackages.first().toMutableSet()
            if (currentAllowed.contains(packageName)) {
                currentAllowed.remove(packageName)
            } else {
                currentAllowed.add(packageName)
            }
            preferenceManager.setAllowedPackages(currentAllowed)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
