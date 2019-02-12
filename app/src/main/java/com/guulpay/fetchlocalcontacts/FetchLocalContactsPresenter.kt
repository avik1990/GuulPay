package com.guulpay.fetchlocalcontacts

import android.content.Context
import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractor
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl


class FetchLocalContactsPresenter(private val context: Context, private val view: FetchLocalContactsContract.View, private val contactsInteractorImpl: GetContactsInteractorImpl) : FetchLocalContactsContract.Presenter, GetContactsInteractor.OnFinishedListener {

    override fun onFinished(list: ArrayList<ContactsModel>) {
        view.handleProgressAlert(false)
        view.fetchlocalContacts(list)
    }

    val TAG = "FetchLocalContactsPresenter"

    init {
    }


    override fun fetchlocalContactsList() {
        view.handleProgressAlert(true)
        contactsInteractorImpl.getContactsList(this)
    }


    override fun start() {
        view.setPresenter(this)
        //jsonAsString = loadJSONFromAsset()
    }

    override fun stop() {
        //  disposable?.dispose()
        view.handleProgressAlert(false)
        /* if (!fetchCountryCodesAsyncTask.isCancelled)
             fetchCountryCodesAsyncTask.cancel(true)*/
    }
}
