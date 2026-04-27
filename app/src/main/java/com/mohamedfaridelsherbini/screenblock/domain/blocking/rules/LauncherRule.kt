package com.mohamedfaridelsherbini.screenblock.domain.blocking.rules

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.mohamedfaridelsherbini.screenblock.domain.blocking.BlockingRule
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LauncherRule @Inject constructor(
    @ApplicationContext private val context: Context
) : BlockingRule {
    override suspend fun shouldBlock(packageName: String): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        val resolveInfos = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return resolveInfos.any { it.activityInfo.packageName == packageName }
    }
}
