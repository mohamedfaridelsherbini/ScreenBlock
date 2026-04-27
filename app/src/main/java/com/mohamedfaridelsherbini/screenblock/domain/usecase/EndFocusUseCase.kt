package com.mohamedfaridelsherbini.screenblock.domain.usecase

import com.mohamedfaridelsherbini.screenblock.domain.FocusSessionManager
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus
import javax.inject.Inject

class EndFocusUseCase @Inject constructor(
    private val focusSessionManager: FocusSessionManager
) {
    operator fun invoke(status: FocusSessionStatus = FocusSessionStatus.CANCELLED) {
        focusSessionManager.endSession(status)
    }
}
