package com.guulpay.countryCodes

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.SearchView
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.countryCodes.adapter.CountryListAdapter
import com.guulpay.countryCodes.models.CountryCodesModel
import com.guulpay.countryCodes.servicecall.FetchCountryCodeRepositoryProvider
import com.guulpay.login.LoginActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.country_codeslist_activity.*
import java.util.*

class CountryCodesListActivity : BaseActivity(), SearchView.OnQueryTextListener, CountryListAdapter.OnCountryCodesItemSelect,
        CountryCodesContract.View, View.OnClickListener {

    lateinit var countryCodesPresenter: CountryCodesPresenter
    lateinit var countryListAdapter: CountryListAdapter
    lateinit var countryCodesList: ArrayList<CountryCodesModel.Prefixlist>
    var appData: AppData? = null

    override fun initPermissions() {
    }

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun initResources() {
        countryCodesList = ArrayList()
        countryCodesList.clear()
        CountryCodesPresenter(this, this, FetchCountryCodeRepositoryProvider.getCountryCodes()).start()
    }

    override fun initListeners() {
        appData = AppData(this@CountryCodesListActivity, Constants.Keys._KeyCryptoPreference)
        imgvwBack.setOnClickListener(this)
        searchView.setOnQueryTextListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.country_codeslist_activity
    }

    override fun onDestroy() {
        countryCodesPresenter.stop()
        super.onDestroy()
    }

    /* CountryListAdapter.OnCountryCodesItemSelect */
    override fun getText(text: String, countrycodeId: String,currency_code: String,countryCodeName:String) {
        val intent = Intent()
        intent.putExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE, text)
        intent.putExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID, countrycodeId)
        intent.putExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CURRENCYCODE, currency_code)
        intent.putExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE_NAME,countryCodeName)
        setResult(Constants.RequestCodes.REQUEST_CODE_COUNTRY, intent)

        /* Added by Rishabh 28/1/2019 */
        setResult(Constants.RequestCodes.REQUEST_CODE_COUNTRY_NAME,intent)
        finish()
    }



    /* View.OnClickListener */
    override fun onClick(v: View?) {
        if (v == imgvwBack) {
            finish()
        }
    }

   /* override fun onResume() {
        super.onResume()
    }*/

    /* SearchView.OnQueryTextListener */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText!!.isEmpty()) {
            countryListAdapter.filter(newText)
        }
        return false
    }

    /* CountryCodesContract.View methods callback */
    override fun countryCodesFetched(list: ArrayList<CountryCodesModel.Prefixlist>) {
        if(list.size>0) {
            countryCodesList.addAll(list)
            countryListAdapter = CountryListAdapter(this, countryCodesList, this)
            lvCountryCodes.adapter = countryListAdapter
        }
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

    override fun setPresenter(presenter: CountryCodesContract.Presenter) {
        countryCodesPresenter = presenter as CountryCodesPresenter
        countryCodesPresenter.fetchCountryCodes()

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