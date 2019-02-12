package com.guulpay.mobilerecharge

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.fetchlocalcontacts.FetchLocalContactsListActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.login.LoginActivity
import com.guulpay.mobilerecharge.model.MsisdninfoResponse
import com.guulpay.mobilerecharge.servicecall.MobileRechargeRepositoryProvider
import com.guulpay.mobilerechargehistory.MobileRechargeHistFragment
import com.guulpay.operatorList.OperatorCodesListActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.payments.fetchContacts.ContactListAdapter
import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import kotlinx.android.synthetic.main.mobile_recharge_fragment_layout.*
import java.util.*


class MobileRechargeFragment : BaseFragment(), MobileRechargeContract.EnterPhoneContract.View,
        SearchView.OnQueryTextListener, ContactListAdapter.OnContactListItemSelect, View.OnClickListener {

    lateinit var contactListAdapter: ContactListAdapter
    lateinit var enterPhonePresenter: MobileRechargePresenter

    var phoneUtil: PhoneNumberUtil? = null
    var appData: AppData? = null
    var operatorId: String = ""
    var isWalletselected: String = "1"
    var countryCodeID: String = ""
    var amount: String? = ""
    var currencyCode: String? = ""

    companion
    object {
        var flagfrom: Int = 0
        var phoneno_result: String? = ""
        var callprefix: String? = ""
        var rechargeamt: String? = ""
        const val CLASS_NAME = "MobileRechargeFragment"
        fun newInstance(): Fragment {
            return MobileRechargeFragment()
        }
    }


    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.mobile_recharge_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        phoneUtil = PhoneNumberUtil.getInstance()
        appData = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        MobileRechargePresenter(this, GetContactsInteractorImpl(context?.contentResolver!!), MobileRechargeRepositoryProvider.getRechargeRepository(appData!!.remember_token), appData!!).start()

        chkWallet?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isWalletselected = "1"
            } else {
                isWalletselected = "0"
            }

        }

    }

    override fun initListeners() {
        fabTransactions.setOnClickListener(this)
        tvCountryCode.setOnClickListener(this)
        spOperator.setOnClickListener(this)
        btProceedRecharge.setOnClickListener(this)
        tvWalletAmt.text = appData!!.currency_symbol + " " + appData!!.register_amount

        etPhoneNumber.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etPhoneNumber.right - etPhoneNumber.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    // your action here
                    val intent = Intent(activity, FetchLocalContactsListActivity::class.java)
                    startActivityForResult(intent, Constants.RequestCodes.REQUEST_PHONENO)
                    return@OnTouchListener true
                }
            }
            false
        })

        etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().length > 7) {
                    tvConfirmPassword.visibility = View.VISIBLE
                    llConfirmPassword.visibility = View.VISIBLE
                    etPhoneNumberConfirm.setText("")
                    //Utils.showToast(context, "Hello")
                } else {
                    etPhoneNumberConfirm.setText("")
                    tvConfirmPassword.visibility = View.GONE
                    llConfirmPassword.visibility = View.GONE
                }

            }
        })

        etPhoneNumberConfirm.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!etPhoneNumber.text.isEmpty() && !etPhoneNumberConfirm.text.isEmpty()) {
                    if (p0.toString().equals(etPhoneNumber.text.toString())) {
                        enterPhonePresenter.callMsisdninfoeApi(tvCountryCode.text.toString().replace("+", "").trim(), etPhoneNumber.text.toString(), appData!!)
                    }
                } else {
                    llSpinnerView.visibility = View.GONE
                    tvTopupamt.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(v: View?) {
        if (v == tvCountryCode) {
            val intent = Intent(activity, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
        } else if (v == fabTransactions) {
            (activity as HomeActivity).setUpScreenUiForFragment(MobileRechargeHistFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, MobileRechargeHistFragment.newInstance(MobileRechargeFragment.CLASS_NAME),
                    R.id.flFragmentContainerHome, true, MobileRechargeHistFragment.CLASS_NAME)
        } else if (v == spOperator) {
            val intent = Intent(activity, OperatorCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_OPERATOR_ID)
        } else if (v == btProceedRecharge) {
            if (etPhoneNumber.text.toString().isEmpty()) {
                Utils.showToast(context, "Enter valid mobile number.")
                return
            }
            if (etPhoneNumberConfirm.text.toString().isEmpty()) {
                Utils.showToast(context, "Enter valid mobile number.")
                return
            }
            if (!etPhoneNumber.text.toString().equals(etPhoneNumberConfirm.text.toString())) {
                Utils.showToast(context, "Phone Number Doesn't matches")
                return
            }
            if (amount.toString().isEmpty()) {
                Utils.showToast(context, "Enter recharge amount.")
                return
            }

            if (appData!!.register_amount.toDouble() >= amount!!.toDouble()) {
                enterPhonePresenter.callRechargeApi(user_id = appData!!.user_id, recharege_currency_code = currencyCode!!, mobile_no = etPhoneNumber.text.toString().trim(), amount = amount!!, isWalletselected = isWalletselected, callprefix = tvCountryCode.text.toString().trim().replace("+", ""), appData = appData!!)
            } else {
                Utils.showToast(context, "Wallet balance is low.")
            }

        }
    }


    /* ContactListAdapter.OnContactListItemSelect callback method*/
    override fun getNumber(text: String) {
        //text?.let {  searchVwMobile.setQuery(text,false) }
    }

    /* SearchView.OnQueryTextListener */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { contactListAdapter.filter(newText) }
        return false
    }

    override fun showDefaultCountryCode(country_id: String) {
        country_id?.let { tvCountryCode.text = it }
        enterPhonePresenter.callCountrylistApi()
    }

    /* RequestMoneyContract.EnterPhoneContract.View callback methods */
    override fun onContactListFetched(list: ArrayList<ContactsModel>) {

    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
        Utils.showToast(context, "Recharged successfully")
        activity?.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            flagfrom = Constants.RequestCodes.REQUEST_CODE_COUNTRY
            try {

                val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
                countryCode?.let { tvCountryCode.text = countryCode }
                countryCodeID = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)!!
                countryCode?.let { Log.d("CountryCodeID", countryCodeID) }

                currencyCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CURRENCYCODE)!!
                currencyCode?.let { Log.d("currencyCode", currencyCode) }
                etPhoneNumberConfirm.setText("")
            } catch (e: Exception) {

            }
        } else if (requestCode == Constants.RequestCodes.REQUEST_PHONENO) {
            flagfrom = Constants.RequestCodes.REQUEST_PHONENO

            var phoneno = data?.getStringExtra(Constants.Keys.KEY_SELECTED_PHONE_NUMBER)
            phoneno?.let {
                if (phoneno.contains("+")) {
                    val numberProto = phoneUtil!!.parse(phoneno, "")
                    etPhoneNumber.setText(numberProto.nationalNumber.toString())
                } else {
                    etPhoneNumber.setText(phoneno)
                }
            }
        } else if (requestCode == Constants.RequestCodes.REQUEST_CODE_OPERATOR_ID) {
            flagfrom = Constants.RequestCodes.REQUEST_CODE_OPERATOR_ID
            try {
                var operatorname = data?.getStringExtra(Constants.Keys.KEY_SELECTED_OPERATOR_NAME)
                operatorname?.let { spOperator.setText(operatorname.toString()) }
                operatorId = data?.getStringExtra(Constants.Keys.KEY_SELECTED_OPERATOR_CODE)!!
                Log.d("operatorId", operatorId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == Constants.RequestCodes.REQUEST_RECHARGEREPEAT) {
            flagfrom = Constants.RequestCodes.REQUEST_RECHARGEREPEAT
            try {
                phoneno_result = data?.getStringExtra("phoneno")
                callprefix = data?.getStringExtra("callprefix")
                rechargeamt = data?.getStringExtra("rechargeamt")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (flagfrom == Constants.RequestCodes.REQUEST_RECHARGEREPEAT) {
            etPhoneNumber.setText(phoneno_result)
            etPhoneNumberConfirm.setText(phoneno_result)
            if (!callprefix!!.isEmpty()) {
                tvCountryCode.text = callprefix
            }
        }
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }


    override fun onPause() {
        super.onPause()
        phoneno_result = ""
        callprefix = ""
    }

    override fun setPresenter(presenter: MobileRechargeContract.EnterPhoneContract.Presenter) {
        enterPhonePresenter = presenter as MobileRechargePresenter
        enterPhonePresenter.getDefaultCountryCode()
        enterPhonePresenter.fetchContacts()
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(lnRecharge, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        try {
            Utils.showSnackbar(lnRecharge, msg, 3000)
        } catch (e: Exception) {

        }
    }

    override fun getCountryIdfromApi(country_id: String, currency_code: String) {
        countryCodeID = country_id
        currencyCode = currency_code
        // Log.d("CountryCodeID", countryCodeID + "-" + currencyCode)
    }

    override fun getTelephonyManager(): TelephonyManager {
        return context!!.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun getCountryCodesArray(): Array<String> {
        return resources.getStringArray(R.array.countryCodes)
    }

    override fun getTopUpAmts(msisdninfoResponse: MsisdninfoResponse.Operatorlist) {
        if (msisdninfoResponse.productList!!.size > 0) {
            llSpinnerView.visibility = View.VISIBLE
            tvTopupamt.visibility = View.VISIBLE

            val arraysize = msisdninfoResponse.productList!!.size + 1
            val someStrings = Array(arraysize) { "it = $it" }
            someStrings[0] = "$currencyCode Choose Top Up Amount"

            for (i in msisdninfoResponse.productList!!.indices) {
                someStrings[i + 1] = msisdninfoResponse.productList!![i]
            }

            val array = arrayOfNulls<String>(msisdninfoResponse.productList!!.size)
            msisdninfoResponse.productList!!.toArray(array)

            val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, someStrings)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spChooseAmount.adapter = adapter

            //............spinner item selected listener........
            spChooseAmount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                    amount = if (pos != 0) {
                        val selectedItemText = parent.getItemAtPosition(pos).toString()
                        selectedItemText
                    } else {
                        ""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //Another interface callback
                }

            }
        } else {
            tvTopupamt.visibility = View.GONE
            llSpinnerView.visibility = View.GONE
        }

    }

    override fun globalLogout() {
        appData!!.cleardata(context!!, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(context, LoginActivity::class.java)
        activity!!.startActivity(intent)
        activity!!.finishAffinity()
    }

}