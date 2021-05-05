@file:Suppress("DEPRECATION")

package com.ketee_jishs.contacts.ui.contacts_search

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ketee_jishs.contacts.databinding.FragmentContactsSearchBinding
import com.ketee_jishs.contacts.retrofit.ContactsServerResponse
import com.ketee_jishs.contacts.ui.ContactsViewModel
import com.ketee_jishs.contacts.utils.AppState
import com.ketee_jishs.contacts.utils.color_interactor.ColorInteractorImpl
import com.ketee_jishs.contacts.utils.toast

@Suppress("DEPRECATION")
class ContactsSearchFragment : Fragment() {

    private lateinit var binding: FragmentContactsSearchBinding
    private lateinit var contactsAdapter: ContactsAutoCompleteAdapter
    private val viewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsSearchBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.executePendingBindings()
        autoCompleteTextViewFunctions()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactsAdapter = ContactsAutoCompleteAdapter(
            requireContext(),
            arrayListOf(),
            ColorInteractorImpl(requireContext())
        )
        binding.contactsInputAutoCompleteTextView.setAdapter(contactsAdapter)
        viewModel.setVisibility(false)
        getData()
    }
    
    private fun textWatcher(list: List<ContactsServerResponse>): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.contactsInputAutoCompleteTextView.setOnItemClickListener { _, _, _, _ ->
                    val selectedContact =
                        binding.contactsInputAutoCompleteTextView.text.toString()
                    for (i in list.indices) {
                        if (list[i].name == selectedContact) {
                            viewModel.setFoundedContactData(
                                true,
                                list[i].name,
                                list[i].phone
                            )
                        }
                    }
                    inputLayoutClearFocus()
                }
                ContactsAutoCompleteAdapter.inputtedText = s.toString()
                setContactsNotFoundViewVisibility()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                resultViewsInvisible()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setContactsNotFoundViewVisibility()
                viewModel.setFoundedContactData(false, "", "")
            }
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getData() {
        viewModel.getData().observe(
            this@ContactsSearchFragment,
            Observer<AppState> { renderData(it) }
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                contactsAdapter.replaceData(appState.contactsServerResponse)
                binding.contactsInputAutoCompleteTextView.addTextChangedListener(
                    textWatcher(
                        appState.contactsServerResponse
                    )
                )
            }
            is AppState.Loading -> { }
            is AppState.Error -> {
                toast(appState.error.message)
            }
        }
    }

    private fun setContactsNotFoundViewVisibility() {
        when (binding.contactsInputAutoCompleteTextView.isPopupShowing) {
            true -> { viewModel.setVisibility(false) }
            false -> { viewModel.setVisibility(true) }
        }
    }

    private fun autoCompleteTextViewFunctions() {
        binding.fragmentContactsSearch.setOnClickListener { inputLayoutClearFocus() }
        binding.contactsInputLayout.setEndIconOnClickListener {
            binding.contactsInputAutoCompleteTextView.text = null
            inputLayoutClearFocus()
            resultViewsInvisible()
        }
    }

    private fun inputLayoutClearFocus() {
        val manager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        binding.contactsInputAutoCompleteTextView.clearFocus()
        binding.contactsInputLayout.clearFocus()
    }

    private fun resultViewsInvisible() {
        viewModel.setVisibility(false)
        viewModel.setFoundedContactData(false, "", "")
    }
}