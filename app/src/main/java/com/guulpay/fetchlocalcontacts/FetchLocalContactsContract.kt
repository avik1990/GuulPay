package com.guulpay.fetchlocalcontacts

import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView
import com.guulpay.payments.fetchContacts.ContactsModel

interface FetchLocalContactsContract {

    interface View: BaseView<Presenter> {
        fun fetchlocalContacts(list:ArrayList<ContactsModel>)
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun fetchlocalContactsList()
    }
}