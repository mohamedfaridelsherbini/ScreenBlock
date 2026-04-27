package com.mohamedfaridelsherbini.screenblock.domain.blocking.rules

import com.mohamedfaridelsherbini.screenblock.data.PreferenceManager
import com.mohamedfaridelsherbini.screenblock.domain.blocking.BlockingRule
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AllowedAppsRule @Inject constructor(
    private val preferenceManager: PreferenceManager
) : BlockingRule {
    override suspend fun shouldBlock(packageName: String): Boolean {
        val allowedPackages = preferenceManager.allowedPackages.first()
        return !allowedPackages.contains(packageName)
    }
}
