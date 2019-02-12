package com.guulpay.payments.request.enterPhone

import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractor
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import com.guulpay.payments.request.RequestMoneyContract

class EnterPhoneRequestMoneyPresenter(private val view: RequestMoneyContract.EnterPhoneContract.View,
                                      private val contactsInteractorImpl: GetContactsInteractorImpl) :
        RequestMoneyContract.EnterPhoneContract.Presenter, GetContactsInteractor.OnFinishedListener {

    override fun fetchContacts() {
        if (view.isFragmentAlive()){
            view.handleProgressAlert(true)
            contactsInteractorImpl.getContactsList(this)
        }
    }

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {

    }

    /* GetContactsInteractor.OnFinishedListener callback methods */
    override fun onFinished(list: ArrayList<ContactsModel>) {
        view.handleProgressAlert(false)
        view.onContactListFetched(list)
    }
}