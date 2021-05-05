package com.ketee_jishs.contacts.retrofit

import com.ketee_jishs.contacts.utils.DYNAMIC_URL
import retrofit2.Call
import retrofit2.http.GET

interface ContactsAPI {
    @GET(DYNAMIC_URL)
    fun getContacts(): Call<ArrayList<ContactsServerResponse>>
}