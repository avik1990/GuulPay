package com.guulpay.login

import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface LoginContract {

    interface View : BaseView<Presenter> {
        fun getContext(): AppCompatActivity ///to get activity context from presenter
        fun getCountryCodesArray(): Array<String> // From strings.xml file
        fun getTelephonyManager(): TelephonyManager // Get telephony manager
        fun showDefaultCountryCode(code: String) // To show default country code selected at first
        fun showPasscode() // show passcode
        fun hidePasscode() // hide passcode
        fun gotoOtpScreen()
        fun showPhoneNumberScreen() // from the second time, hide phone number layout after successfully installed and registered
        fun hidePhoneNumberScreen()  // show phone number layout on first installed
        fun phoneNumberValidatedSuccessfully() // When phone number is validated using API call
        fun phoneNumberValidationFailed() // checks whether Phone number is blank or not
        fun passcodeValidationFailed() // checks whether Passcode is blank or not
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun getDefaultCountryCode()
        fun showHidePasscode()
        fun validatePhoneNumber(number: String)
        fun callLoginApi(isFingerPrintValidated: Boolean, phoneno: String, passcode_1: String, passcode_2: String, passcode_3: String, passcode_4: String)
    }
}