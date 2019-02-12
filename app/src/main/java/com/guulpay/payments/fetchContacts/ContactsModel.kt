package com.guulpay.payments.fetchContacts

data class ContactsModel(val name:String, val number:String) : Comparable<ContactsModel>{

    override fun compareTo(other: ContactsModel): Int {
        return name.compareTo(other.name)
    }
}