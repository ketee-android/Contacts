package com.ketee_jishs.contacts.ui.contacts_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketee_jishs.contacts.databinding.ContactRecyclerItemBinding
import com.ketee_jishs.contacts.retrofit.ContactsServerResponse

class ContactsRecyclerAdapter(
    private var data: List<ContactsServerResponse> = arrayListOf()
) : RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    fun replaceData(data: List<ContactsServerResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContactRecyclerItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(private var binding: ContactRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ContactsServerResponse) {
            binding.contact = data
            binding.executePendingBindings()
        }
    }
}