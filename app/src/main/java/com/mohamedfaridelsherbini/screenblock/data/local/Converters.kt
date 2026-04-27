package com.mohamedfaridelsherbini.screenblock.data.local

import androidx.room.TypeConverter
import com.mohamedfaridelsherbini.screenblock.domain.model.FocusSessionStatus

class Converters {
    @TypeConverter
    fun fromStatus(status: FocusSessionStatus): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(value: String): FocusSessionStatus {
        return FocusSessionStatus.valueOf(value)
    }
}
