package com.guulpay.signup

import android.content.Context
import android.telephony.TelephonyManager
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface SignupContract {

    interface View : BaseView<Presenter> {
        fun getContext(): Context
        fun getCountryCodesArray(): Array<String> // From strings.xml file
        fun getTelephonyManager(): TelephonyManager // Get telephony manager
        fun showDefaultCountryCode(code: String) // To show default country code selected at first
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        /* Handle validations */
        fun fieldsValidationFailed(msg: String)

        fun enableButton()
        fun disableButton()
        fun getCountryIdfromApi(countryid: String)
    }

    interface Presenter : BasePresenter {
        fun checkFieldsValidation(name: String, mobile: String, email: String, passcode: String,
                                  confirmPasscode: String, isChecked: Boolean, countrycode: String)

        fun callCountrylistApi()
        fun getDefaultCountryCode()
        fun signupUser()
        fun goToPrivacyPolicy()
        fun checkTermsConditions()
        fun submitOtp()
        fun callsignUpApi(name: String, mobile: String, email: String, passcode: String, c_passcode: String, countrycode: String, device_token: String, user_role: String)

    }
}