package com.guulpay.operatorList

import android.content.Intent
import android.view.View
import android.widget.SearchView
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.login.LoginActivity
import com.guulpay.operatorList.adapter.OperatorListAdapter
import com.guulpay.operatorList.models.OperatorModel
import com.guulpay.operatorList.servicecall.OperatorCodeRepositoryProvider
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.country_codeslist_activity.*

class OperatorCodesListActivity : BaseActivity(), SearchView.OnQueryTextListener, OperatorListAdapter.OnCountryCodesItemSelect,
        OperatorCodesContract.View, View.OnClickListener {


    lateinit var OperatorCodesPresenter: OperatorCodesPresenter
    lateinit var countryListAdapter: OperatorListAdapter
    lateinit var countryCodesList: ArrayList<OperatorModel.Operatorlist>
    var appData: AppData? = null

    override fun initPermissions() {
    }

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun initResources() {
        countryCodesList = ArrayList()
        countryCodesList.clear()
        appData= AppData(this, Constants.Keys._KeyCryptoPreference)
        OperatorCodesPresenter(this, this, OperatorCodeRepositoryProvider.getOperatorList(appData!!.remember_token), appData!!).start()
    }

    override fun initListeners() {
        imgvwBack.setOnClickListener(this)
        searchView.setOnQueryTextListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.operator_list_activity
    }

    override fun onDestroy() {
        OperatorCodesPresenter.stop()
        super.onDestroy()
    }

    /* CountryListAdapter.OnCountryCodesItemSelect */
    override fun getText(operatorname: String, operatorId: String) {
        val intent = Intent()
        intent.putExtra(Constants.Keys.KEY_SELECTED_OPERATOR_NAME, operatorname)
        intent.putExtra(Constants.Keys.KEY_SELECTED_OPERATOR_CODE, operatorId)
        setResult(Constants.RequestCodes.REQUEST_CODE_OPERATOR_ID, intent)
        //setResult(Constants.RequestCodes.REQUEST_CODE_COUNTRY_ID, intent)
        finish()
    }

    /* View.OnClickListener */
    override fun onClick(v: View?) {
        if (v == imgvwBack) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        OperatorCodesPresenter.fetchOperators()
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


    /* OperatorCodesContract.View methods callback */
    override fun operatorFetched(list: ArrayList<OperatorModel.Operatorlist>) {
        countryCodesList.addAll(list)
        countryListAdapter = OperatorListAdapter(this, countryCodesList, this)
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

    override fun setPresenter(presenter: OperatorCodesContract.Presenter) {
        OperatorCodesPresenter = presenter as OperatorCodesPresenter
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