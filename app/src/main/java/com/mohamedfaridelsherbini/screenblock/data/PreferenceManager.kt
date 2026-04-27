package com.mohamedfaridelsherbini.screenblock.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "focus_prefs")

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val ALLOWED_PACKAGES = stringSetPreferencesKey("allowed_packages")
        val ONBOARDING_COMPLETED = androidx.datastore.preferences.core.booleanPreferencesKey("onboarding_completed")
        val STRICT_MODE_ENABLED = androidx.datastore.preferences.core.booleanPreferencesKey("strict_mode_enabled")
    }

    val allowedPackages: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[Keys.ALLOWED_PACKAGES] ?: emptySet()
    }

    suspend fun setAllowedPackages(packages: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ALLOWED_PACKAGES] = packages
        }
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    val strictModeEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.STRICT_MODE_ENABLED] ?: true
    }

    suspend fun setStrictModeEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.STRICT_MODE_ENABLED] = enabled
        }
    }
}
