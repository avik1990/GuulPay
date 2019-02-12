package com.guulpay.payments.send.enterPhone

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.SearchView
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import com.guulpay.payments.PaymentActivity
import com.guulpay.payments.fetchContacts.ContactListAdapter
import com.guulpay.payments.scanQrCode.ScanQrFragment
import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import com.guulpay.payments.send.SendMoneyContract
import com.guulpay.payments.send.paymentStatus.SendMoneyPaymentStatus
import kotlinx.android.synthetic.main.enter_phone_sendmoney.*

class EnterPhoneSendMoneyFragment:BaseFragment(), View.OnClickListener, SendMoneyContract.EnterPhone.View,
        ContactListAdapter.OnContactListItemSelect, SearchView.OnQueryTextListener{

    lateinit var enterPhoneSendMoneyPresenter: EnterPhoneSendMoneyPresenter
    lateinit var contactListAdapter: ContactListAdapter

    companion object {
        const val CLASS_NAME = "EnterPhoneSendMoneyFragment"
        fun newInstance(): Fragment {
            return EnterPhoneSendMoneyFragment()
        }
    }
    override fun getLayoutView(): Int {
        return R.layout.enter_phone_sendmoney
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
       EnterPhoneSendMoneyPresenter(this, GetContactsInteractorImpl(context?.contentResolver!!)).start()
    }

    override fun initListeners() {
        btNextSendMoney.setOnClickListener(this)
        imgvwQrSendMoney.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwQrSendMoney){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, ScanQrFragment.newInstance(Constants.Values.TO_SEND_MONEY),
                    R.id.flPaymentContainer, false, ScanQrFragment.CLASS_NAME)
        }
        else if (v == btNextSendMoney){
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, SendMoneyPaymentStatus.CLASS_NAME)
            startActivity(intent)
            activity?.finish()
        }
    }

    /* SearchView.OnQueryTextListener */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        newText?.let { contactListAdapter.filter(newText) }
        return false
    }

    /* ContactListAdapter.OnContactListItemSelect callback methods */
    override fun getNumber(text: String) {
        text?.let {  searchVwMobile.setQuery(text,false)}
    }

    /* SendMoneyContract.EnterPhone.View callback methods */
    override fun onContactListFetched(list: ArrayList<ContactsModel>) {
        /* Contact list */
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

    override fun setPresenter(presenter: SendMoneyContract.EnterPhone.Presenter) {
        enterPhoneSendMoneyPresenter = presenter as EnterPhoneSendMoneyPresenter
        enterPhoneSendMoneyPresenter.fetchContacts()
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