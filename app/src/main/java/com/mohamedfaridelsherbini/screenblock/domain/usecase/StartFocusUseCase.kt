package com.mohamedfaridelsherbini.screenblock.domain.usecase

import com.mohamedfaridelsherbini.screenblock.domain.FocusSessionManager
import javax.inject.Inject

class StartFocusUseCase @Inject constructor(
    private val focusSessionManager: FocusSessionManager
) {
    operator fun invoke(durationMinutes: Int) {
        focusSessionManager.startSession(durationMinutes)
    }
}
