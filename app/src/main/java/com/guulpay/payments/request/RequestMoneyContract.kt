package com.guulpay.payments.request

import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView
import com.guulpay.payments.fetchContacts.ContactsModel

interface RequestMoneyContract {

    /* For enter mobile number screen */
    interface EnterPhoneContract{
        interface View: BaseView<Presenter> {
            fun onContactListFetched(list:ArrayList<ContactsModel>)
            fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
        }
        interface Presenter : BasePresenter {
            fun fetchContacts()
        }
    }
}