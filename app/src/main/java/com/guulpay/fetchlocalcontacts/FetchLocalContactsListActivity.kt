package com.guulpay.fetchlocalcontacts

import android.content.Intent
import android.view.View
import android.widget.SearchView
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.login.LoginActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.payments.fetchContacts.ContactListAdapter
import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import kotlinx.android.synthetic.main.country_codeslist_activity.*

class FetchLocalContactsListActivity : BaseActivity(), SearchView.OnQueryTextListener, ContactListAdapter.OnContactListItemSelect,
        FetchLocalContactsContract.View, View.OnClickListener {

    lateinit var fetchLocalContactsPresenter: FetchLocalContactsPresenter
    lateinit var countryListAdapter: ContactListAdapter
    lateinit var countryCodesList: ArrayList<ContactsModel>
    var appData: AppData? = null

    override fun initPermissions() {

    }

    private val loader by lazy {
        LoaderDialog(this)
    }


    override fun initResources() {
        appData = AppData(this@FetchLocalContactsListActivity, Constants.Keys._KeyCryptoPreference)
        countryCodesList = ArrayList()
        countryCodesList.clear()
    }

    override fun initListeners() {
        FetchLocalContactsPresenter(this, this, GetContactsInteractorImpl(applicationContext?.contentResolver!!)).start()
        fetchLocalContactsPresenter.fetchlocalContactsList()
        imgvwBack.setOnClickListener(this)
        searchView.setOnQueryTextListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.contact_list_activity
    }

    override fun onDestroy() {
        fetchLocalContactsPresenter.stop()
        super.onDestroy()
    }

    /* CountryListAdapter.OnCountryCodesItemSelect */
    /*  override fun getText(text: String, countrycodeId: String) {
          val intent = Intent()
          intent.putExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE, text)
          intent.putExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID, countrycodeId)
          setResult(Constants.RequestCodes.REQUEST_CODE_COUNTRY, intent)
          //setResult(Constants.RequestCodes.REQUEST_CODE_COUNTRY_ID, intent)
          finish()
      }*/

    override fun getNumber(text: String) {
        val intent = Intent()
        intent.putExtra(Constants.Keys.KEY_SELECTED_PHONE_NUMBER, text)
        setResult(Constants.RequestCodes.REQUEST_PHONENO, intent)
        finish()
    }

    /* View.OnClickListener */
    override fun onClick(v: View?) {
        if (v == imgvwBack) {
            finish()
        }
    }


    /* SearchView.OnQueryTextListener */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            countryListAdapter.filter(newText)
        }
        return false
    }

    override fun fetchlocalContacts(list: ArrayList<ContactsModel>) {
        countryCodesList.addAll(list)
        countryListAdapter = ContactListAdapter(this, countryCodesList, this)
        lvCountryCodes.adapter = countryListAdapter
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun isFragmentAlive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isActivityRunning(): Boolean {
        return isActivityVisible
    }

    override fun setPresenter(presenter: FetchLocalContactsContract.Presenter) {
        fetchLocalContactsPresenter = presenter as FetchLocalContactsPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(this)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llParentCountryCodes, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llParentCountryCodes, msg, 3000)
    }

    override fun globalLogout() {
        appData!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}