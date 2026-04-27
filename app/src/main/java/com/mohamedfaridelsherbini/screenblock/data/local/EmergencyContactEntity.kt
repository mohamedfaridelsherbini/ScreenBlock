package com.mohamedfaridelsherbini.screenblock.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val phoneNumber: String
)
