package com.ketee_jishs.contacts.retrofit

import com.google.gson.annotations.SerializedName

class ContactsServerResponse (
    @field:SerializedName("name") var name: String,
    @field:SerializedName("phone") var phone: String
)