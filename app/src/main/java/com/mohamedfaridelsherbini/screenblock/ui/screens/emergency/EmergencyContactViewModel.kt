package com.mohamedfaridelsherbini.screenblock.ui.screens.emergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedfaridelsherbini.screenblock.data.repository.EmergencyContactRepository
import com.mohamedfaridelsherbini.screenblock.domain.model.EmergencyContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyContactViewModel @Inject constructor(
    private val repository: EmergencyContactRepository
) : ViewModel() {

    val contacts: StateFlow<List<EmergencyContact>> = repository.emergencyContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.addContact(contact)
        }
    }

    fun removeContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.removeContact(contact)
        }
    }
}
