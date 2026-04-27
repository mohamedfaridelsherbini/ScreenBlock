package com.mohamedfaridelsherbini.screenblock.data.repository

import com.mohamedfaridelsherbini.screenblock.data.local.EmergencyContactDao
import com.mohamedfaridelsherbini.screenblock.data.local.EmergencyContactEntity
import com.mohamedfaridelsherbini.screenblock.domain.model.EmergencyContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyContactRepository @Inject constructor(
    private val contactDao: EmergencyContactDao
) {
    val emergencyContacts: Flow<List<EmergencyContact>> = contactDao.getAllContacts().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun addContact(contact: EmergencyContact) {
        contactDao.insertContact(contact.toEntity())
    }

    suspend fun removeContact(contact: EmergencyContact) {
        contactDao.deleteContact(contact.toEntity())
    }

    private fun EmergencyContactEntity.toDomain() = EmergencyContact(
        id = id,
        displayName = displayName,
        phoneNumber = phoneNumber
    )

    private fun EmergencyContact.toEntity() = EmergencyContactEntity(
        id = id,
        displayName = displayName,
        phoneNumber = phoneNumber
    )
}
