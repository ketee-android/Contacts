@file:Suppress("DEPRECATION")

package com.ketee_jishs.contacts.ui.contacts_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ketee_jishs.contacts.databinding.FragmentContactsListBinding
import com.ketee_jishs.contacts.ui.ContactsViewModel
import com.ketee_jishs.contacts.utils.AppState
import com.ketee_jishs.contacts.utils.toast

@Suppress("DEPRECATION")
class ContactsListFragment : Fragment() {

    private lateinit var binding: FragmentContactsListBinding
    private val contactsAdapter = ContactsRecyclerAdapter(arrayListOf())
    private val viewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.contactsList.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = contactsAdapter
        }
        getData()
        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getData() {
        viewModel.getData().observe(
            this@ContactsListFragment,
            Observer<AppState> { renderData(it) }
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                contactsAdapter.replaceData(appState.contactsServerResponse)
                viewModel.setVisibility(true)
            }
            is AppState.Loading -> {
                viewModel.setVisibility(false)
            }
            is AppState.Error -> {
                toast(appState.error.message)
            }
        }
    }
}