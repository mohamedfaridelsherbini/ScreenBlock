package com.mohamedfaridelsherbini.screenblock.domain.blocking.rules

import com.mohamedfaridelsherbini.screenblock.domain.blocking.BlockingRule
import javax.inject.Inject

class AssistantRule @Inject constructor() : BlockingRule {
    private val assistantPackages = setOf(
        "com.google.android.googlequicksearchbox",
        "com.google.android.apps.googleassistant",
        "com.samsung.android.bixby.agent",
        "com.microsoft.cortana"
    )

    override suspend fun shouldBlock(packageName: String): Boolean {
        return assistantPackages.any { packageName.contains(it) }
    }
}
