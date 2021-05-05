package com.ketee_jishs.contacts.retrofit

import com.google.gson.GsonBuilder
import com.ketee_jishs.contacts.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContactsRetrofitImpl {
    fun getRetrofitImpl(): ContactsAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return retrofit.create(ContactsAPI::class.java)
    }
}