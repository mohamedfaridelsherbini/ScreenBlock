package com.mohamedfaridelsherbini.screenblock.domain.blocking

interface BlockingRule {
    suspend fun shouldBlock(packageName: String): Boolean
}
