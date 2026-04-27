package com.mohamedfaridelsherbini.screenblock.ui.screens.emergency

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mohamedfaridelsherbini.screenblock.domain.model.EmergencyContact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactScreen(
    navController: NavController,
    viewModel: EmergencyContactViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsState()
    val context = LocalContext.current

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let {
            val contact = getContactDetails(context, it)
            contact?.let { viewModel.addContact(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Contacts") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { contactPickerLauncher.launch(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(contacts) { contact ->
                ContactItem(
                    contact = contact,
                    onDelete = { viewModel.removeContact(contact) }
                )
            }
        }
    }
}

@Composable
fun ContactItem(contact: EmergencyContact, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.displayName, style = MaterialTheme.typography.bodyLarge)
            Text(text = contact.phoneNumber, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@SuppressLint("Range")
private fun getContactDetails(context: Context, contactUri: Uri): EmergencyContact? {
    var contact: EmergencyContact? = null
    val cursor = context.contentResolver.query(contactUri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
            val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            
            var phoneNumber = ""
            if (it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                val phones = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null,
                    null
                )
                phones?.use { pCursor ->
                    if (pCursor.moveToFirst()) {
                        phoneNumber = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                }
            }
            contact = EmergencyContact(id, name, phoneNumber)
        }
    }
    return contact
}
