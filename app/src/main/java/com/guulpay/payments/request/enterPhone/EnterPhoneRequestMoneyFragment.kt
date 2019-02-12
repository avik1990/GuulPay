package com.guulpay.payments.request.enterPhone

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.SearchView
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.payments.PaymentActivity
import com.guulpay.payments.fetchContacts.ContactListAdapter
import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import com.guulpay.payments.request.RequestMoneyContract
import kotlinx.android.synthetic.main.enter_phone_number_requestmoney.*

class EnterPhoneRequestMoneyFragment : BaseFragment(), RequestMoneyContract.EnterPhoneContract.View,
        SearchView.OnQueryTextListener, ContactListAdapter.OnContactListItemSelect {

    lateinit var contactListAdapter: ContactListAdapter
    lateinit var enterPhonePresenter: EnterPhoneRequestMoneyPresenter

    companion object {
        const val CLASS_NAME = "EnterPhoneRequestMoneyFragment"
        fun newInstance(): Fragment {
            return EnterPhoneRequestMoneyFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.enter_phone_number_requestmoney
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        EnterPhoneRequestMoneyPresenter(this, GetContactsInteractorImpl(context?.contentResolver!!)).start()
    }

    override fun initListeners() {

    }

    /* ContactListAdapter.OnContactListItemSelect callback method*/
    override fun getNumber(text: String) {
        text?.let {  searchVwMobile.setQuery(text,false) }
    }

    /* SearchView.OnQueryTextListener */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { contactListAdapter.filter(newText) }
        return false
    }

    /* RequestMoneyContract.EnterPhoneContract.View callback methods */
    override fun onContactListFetched(list: ArrayList<ContactsModel>) {
        /* Contact list adapter */
        contactListAdapter = ContactListAdapter(activity, list,this)
        listVwContactList.adapter = contactListAdapter
        searchVwMobile.setOnQueryTextListener(this)
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
       if (showingStatus){
           progressBar.visibility = View.VISIBLE
           listVwContactList.visibility = View.GONE
       }
        else{
           progressBar.visibility = View.GONE
           listVwContactList.visibility = View.VISIBLE
       }
    }

    override fun goToNextPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as PaymentActivity).isActivityVisible
    }

    override fun setPresenter(presenter: RequestMoneyContract.EnterPhoneContract.Presenter) {
        enterPhonePresenter = presenter as EnterPhoneRequestMoneyPresenter
        enterPhonePresenter.fetchContacts()
    }

    override fun isNetworkAvailable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNetworkUnavailableMsg() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun globalLogout() {
        //appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}