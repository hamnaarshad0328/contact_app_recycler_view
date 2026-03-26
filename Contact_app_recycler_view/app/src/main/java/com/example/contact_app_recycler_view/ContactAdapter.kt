package com.example.contact_app_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ContactAdapter(
    private var contactList: MutableList<Contact>,
    private val listener: OnContactActionListener
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contactListFull: List<Contact> = ArrayList(contactList)
    private var currentQuery: String = ""

    interface OnContactActionListener {
        fun onItemClick(position: Int)
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactPhone: TextView = itemView.findViewById(R.id.tvContactPhone)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val ivContactItemImage: ImageView = itemView.findViewById(R.id.ivContactItemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentContact = contactList[position]

        holder.tvContactName.text = currentContact.name
        holder.tvContactPhone.text = currentContact.phone
        
        if (currentContact.imageUri != null) {
            holder.ivContactItemImage.setImageURI(currentContact.imageUri)
        } else {
            holder.ivContactItemImage.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(position)
        }

        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun updateList(newList: MutableList<Contact>) {
        contactListFull = ArrayList(newList)
        filter(currentQuery)
    }

    fun filter(text: String) {
        currentQuery = text
        val filteredList = mutableListOf<Contact>()
        if (text.isEmpty()) {
            filteredList.addAll(contactListFull)
        } else {
            val filterPattern = text.lowercase(Locale.ROOT).trim()
            for (item in contactListFull) {
                if (item.name.lowercase(Locale.ROOT).contains(filterPattern) ||
                    item.phone.contains(filterPattern)
                ) {
                    filteredList.add(item)
                }
            }
        }
        contactList = filteredList
        notifyDataSetChanged()
    }

    fun sortAZ() {
        contactListFull = contactListFull.sortedBy { it.name.lowercase(Locale.ROOT) }
        filter(currentQuery)
    }

    fun sortZA() {
        contactListFull = contactListFull.sortedByDescending { it.name.lowercase(Locale.ROOT) }
        filter(currentQuery)
    }
    
    fun getContactAt(position: Int): Contact {
        return contactList[position]
    }
}