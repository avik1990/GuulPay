package com.guulpay.payments.fetchContacts

interface GetContactsInteractor {

    interface OnFinishedListener {
        fun onFinished(list: ArrayList<ContactsModel>)
    }
    fun getContactsList(onFinishedListener: OnFinishedListener)
}