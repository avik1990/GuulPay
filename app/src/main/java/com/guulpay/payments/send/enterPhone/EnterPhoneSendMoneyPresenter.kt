package com.guulpay.payments.send.enterPhone

import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractor
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import com.guulpay.payments.send.SendMoneyContract

class EnterPhoneSendMoneyPresenter(private val view: SendMoneyContract.EnterPhone.View,
                                   private val contactsInteractorImpl: GetContactsInteractorImpl) : SendMoneyContract.EnterPhone.Presenter,
        GetContactsInteractor.OnFinishedListener {

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