package com.ketee_jishs.contacts.utils

import com.ketee_jishs.contacts.retrofit.ContactsServerResponse

sealed class AppState {
    data class Success(val contactsServerResponse: List<ContactsServerResponse>) : AppState()
    data class Error (val error: Throwable) : AppState()
    data class Loading (val progress: Int?) : AppState()
}