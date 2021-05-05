package com.ketee_jishs.contacts.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketee_jishs.contacts.retrofit.ContactsRetrofitImpl
import com.ketee_jishs.contacts.retrofit.ContactsServerResponse
import com.ketee_jishs.contacts.utils.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val contactsRetrofitImpl: ContactsRetrofitImpl = ContactsRetrofitImpl()
) : ViewModel() {

    var visibility = ObservableField<Boolean>(false)
    var foundedContactVisibility = ObservableField<Boolean>(false)
    var foundedName = ObservableField<String>()
    var foundedPhone = ObservableField<String>()

    fun getData(): LiveData<AppState> {
        sendServerRequest()
        return liveData
    }

    fun setVisibility(isVisible: Boolean) {
        visibility.set(isVisible)
    }

    fun setFoundedContactData(
        visibility: Boolean,
        name: String,
        phone: String
    ) {
        foundedContactVisibility.set(visibility)
        foundedName.set(name)
        foundedPhone.set(phone)
    }

    private fun sendServerRequest() {
        liveData.value = AppState.Loading(null)
        contactsRetrofitImpl.getRetrofitImpl().getContacts().enqueue(object :
            Callback<ArrayList<ContactsServerResponse>> {
            override fun onResponse(
                call: Call<ArrayList<ContactsServerResponse>>,
                response: Response<ArrayList<ContactsServerResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    liveData.value = AppState.Success(response.body()!!)
                } else {
                    val code = response.code()
                    val message = response.message()
                    if (message.isNullOrEmpty()) {
                        liveData.value = AppState.Error(Throwable("Unidetified error"))
                    } else {
                        liveData.value = AppState.Error(Throwable("$code: $message"))
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ContactsServerResponse>>, t: Throwable) {
                liveData.value = AppState.Error(t)
            }
        })
    }
}