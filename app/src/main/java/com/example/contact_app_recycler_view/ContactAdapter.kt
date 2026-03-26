package com.example.contact_app_recycler_view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ContactAdapter(
    private var contactList: MutableList<Contact>,
    private val listener: OnContactActionListener
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var filteredList: MutableList<Contact> = contactList

    interface OnContactActionListener {
        fun onItemClick(contact: Contact)
        fun onEditClick(contact: Contact, position: Int)
        fun onDeleteClick(contact: Contact, position: Int)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactPhone: TextView = itemView.findViewById(R.id.tvContactPhone)
        val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = filteredList[position]

        holder.tvContactName.text = currentContact.name
        holder.tvContactPhone.text = currentContact.phone
        
        if (currentContact.profileImageUri != null) {
            holder.ivProfile.setImageURI(Uri.parse(currentContact.profileImageUri))
        } else {
            holder.ivProfile.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentContact)
        }

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(currentContact, position)
        }

        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(currentContact, position)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredList = if (lowerCaseQuery.isEmpty()) {
            contactList
        } else {
            val resultList = mutableListOf<Contact>()
            for (contact in contactList) {
                if (contact.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    contact.phone.contains(lowerCaseQuery)
                ) {
                    resultList.add(contact)
                }
            }
            resultList
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Contact>) {
        contactList = newList.toMutableList()
        filteredList = contactList
        notifyDataSetChanged()
    }
}
