package com.mohamedfaridelsherbini.screenblock.data.repository

import com.mohamedfaridelsherbini.screenblock.data.local.EmergencyContactDao
import com.mohamedfaridelsherbini.screenblock.data.mapper.toDomain
import com.mohamedfaridelsherbini.screenblock.data.mapper.toEntity
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
}
