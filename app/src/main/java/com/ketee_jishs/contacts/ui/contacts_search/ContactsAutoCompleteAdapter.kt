package com.ketee_jishs.contacts.ui.contacts_search

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.ketee_jishs.contacts.databinding.ContactRecyclerItemBinding
import com.ketee_jishs.contacts.retrofit.ContactsServerResponse
import com.ketee_jishs.contacts.utils.color_interactor.ColorInteractor

class ContactsAutoCompleteAdapter(
    context: Context,
    private var contactsList: List<ContactsServerResponse>,
    private val colorsInteractor: ColorInteractor
) : ArrayAdapter<ContactsServerResponse>(context, 0, contactsList) {

    override fun getFilter(): Filter {
        return contactsFilter
    }

    fun replaceData(data: List<ContactsServerResponse>) {
        contactsList = data
        notifyDataSetChanged()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContactRecyclerItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )

        val contact = getItem(position)
        binding.contact = contact
        if (contact != null) {
            binding.nameTextView.text = highlightedText(contact.name)
            binding.phoneTextView.text = highlightedText(contact.phone)
        }

        return binding.root
    }

    private var contactsFilter: Filter = object : Filter() {
        @SuppressLint("DefaultLocale")
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = ArrayList<ContactsServerResponse>()

            if (constraint == null || constraint.isEmpty()) {
                suggestions.addAll(contactsList)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (contact: ContactsServerResponse in contactsList) {
                    if (
                        contact.name.toLowerCase().startsWith(filterPattern) ||
                        contact.phone.toLowerCase().startsWith(filterPattern)
                    ) {
                        suggestions.add(contact)
                    }
                }
            }
            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results?.values as List<ContactsServerResponse>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            val result = resultValue as ContactsServerResponse
            return result.name
        }
    }

    @SuppressLint("DefaultLocale")
    private fun highlightedText(suggestedText: String): CharSequence {
        val highlighted: Spannable = SpannableString(suggestedText)
        for (i in inputtedText.toCharArray().indices) {
            if (inputtedText.toLowerCase()[i] == suggestedText.toLowerCase().toCharArray()[i]) {
                highlighted.setSpan(
                    ForegroundColorSpan(colorsInteractor.inputtedTextColor),
                    0,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return highlighted
    }

    companion object {
        var inputtedText = ""
    }
}