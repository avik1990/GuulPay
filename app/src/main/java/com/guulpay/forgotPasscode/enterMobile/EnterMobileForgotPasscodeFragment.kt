package com.guulpay.forgotPasscode.enterMobile

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.enterOtp.EnterOtpFragment
import com.guulpay.forgotPasscode.servicecall.ForgotPasscodeRepositoryProvider
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.entermobile_forgot_passcode_fragment.*

class EnterMobileForgotPasscodeFragment : BaseFragment(), ForgotPasscodeContract.EnterMobile.View, View.OnClickListener {

    lateinit var forgotPasscodePresenter: ForgotPasscodePresenter
    var countryID: String = ""
    var appdata: AppData? = null

    companion object {
        const val CLASS_NAME = "EnterMobileForgotPasscodeFragment"
        fun newInstance(): Fragment {
            return EnterMobileForgotPasscodeFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }


    override fun getLayoutView(): Int {
        return R.layout.entermobile_forgot_passcode_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        ForgotPasscodePresenter(this, ForgotPasscodeRepositoryProvider.getForgotPasscodeRepository(), AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)).start()
    }

    override fun initListeners() {
        appdata = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        tvCountryCode.setOnClickListener(this)
        btSubmitEnterMobile.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == btSubmitEnterMobile) {
            if (!etMobile.text.toString().isEmpty()) {
                //forgotPasscodePresenter.submitMobileNumber(etMobile.text.toString())
                appdata?.register_phone = etMobile.text.toString()
                appdata?.register_country_code = countryID
                forgotPasscodePresenter.callForgotpasscode(etMobile.text.toString(), countryID, AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference))
            } else {
                Utils.showSnackbar(llForgotPwdParent, "Enter Your Phone Number", 3000)
            }

        } else if (v == tvCountryCode) {
            val intent = Intent(activity, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
            countryCode?.let { tvCountryCode.text = countryCode }

            val id = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)
            countryCode?.let { countryID = id.toString() }
        }
    }

    /* ForgotPasscodeContract.View callback methods */
    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
        val number = tvCountryCode.text.toString() + " " + etMobile.text
        Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, EnterOtpFragment.newInstance(EnterMobileForgotPasscodeFragment.CLASS_NAME, number),
                R.id.flFragmentContainerHome, true, EnterOtpFragment.CLASS_NAME)
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: ForgotPasscodeContract.EnterMobile.Presenter) {
        forgotPasscodePresenter = presenter as ForgotPasscodePresenter
        forgotPasscodePresenter.getDefaultCountryCode()
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llForgotPwdParent, getString(R.string.networkUnavailable), 3000)

    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llForgotPwdParent, msg, 3000)
    }

    override fun showValidationError() {
        Utils.showSnackbar(llForgotPwdParent, getString(R.string.enterMobileNumber), 3000)
    }

    override fun getCountryCodesArray(): Array<String> {
        return resources.getStringArray(R.array.countryCodes)
    }

    override fun getTelephonyManager(): TelephonyManager {
        return context?.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun showDefaultCountryCode(code: String) {
        tvCountryCode.text = code
    }

    override fun globalLogout() {
       // appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}