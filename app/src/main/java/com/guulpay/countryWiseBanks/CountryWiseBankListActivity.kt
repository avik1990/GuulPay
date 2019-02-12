package com.guulpay.countryWiseBanks

import android.content.Intent
import android.view.View
import android.widget.SearchView
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.countryCodes.adapter.CountryWiseBankListAdapter
import com.guulpay.countryCodes.models.CountryWiseBankModel
import com.guulpay.login.LoginActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.country_codeslist_activity.*


class CountryWiseBankListActivity: BaseActivity(), SearchView.OnQueryTextListener, CountryWiseBankListAdapter.OnCountryCodesItemSelect,
        CountryWiseBanksContract.View, View.OnClickListener {

    lateinit var countryCodesPresenter: CountryWiseBankPresenter
    lateinit var countryListAdapter: CountryWiseBankListAdapter
    lateinit var countryCodesList: ArrayList<CountryWiseBankModel>
    var appData: AppData? = null

    override fun initPermissions() {
    }

    override fun initResources() {
        appData = AppData(this@CountryWiseBankListActivity, Constants.Keys._KeyCryptoPreference)
        countryCodesList = ArrayList()
        CountryWiseBankPresenter(this, this).start()
    }

    override fun initListeners() {

        imgvwBack.setOnClickListener(this)
        searchView.setOnQueryTextListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.bank_codeslist_activity
    }

    override fun onDestroy() {
        countryCodesPresenter.stop()
        super.onDestroy()
    }

    /* CountryListAdapter.OnCountryCodesItemSelect */
    override fun getText(text: String) {
        val intent = Intent()
        intent.putExtra(Constants.Keys.KEY_SELECTED_BANK_CODE, text)
        setResult(Constants.RequestCodes.REQUEST_CODE_BANK, intent)
        finish()
    }

    /* View.OnClickListener */
    override fun onClick(v: View?) {
        if (v == imgvwBack){
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

    /* CountryCodesContract.View methods callback */
    override fun countryBankcsFetched(list: ArrayList<CountryWiseBankModel>) {
        countryCodesList.addAll(list)
        countryListAdapter = CountryWiseBankListAdapter(this, countryCodesList, this)
        lvCountryCodes.adapter = countryListAdapter
    }

    override fun handleProgressAlert(showingStatus: Boolean) {

    }

    override fun goToNextPage() {

    }

    override fun isFragmentAlive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isActivityRunning(): Boolean {
        return isActivityVisible
    }

    override fun setPresenter(presenter: CountryWiseBanksContract.Presenter) {
        countryCodesPresenter = presenter as CountryWiseBankPresenter
        countryCodesPresenter.fetchcountryBanks()
    }

    override fun isNetworkAvailable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNetworkUnavailableMsg() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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