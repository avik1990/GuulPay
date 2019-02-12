package com.guulpay.forgotPasscode.enterMobile

import android.telephony.TelephonyManager
import com.guulpay.others.AppData
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface ForgotPasscodeContract {

    interface EnterMobile {
        interface View : BaseView<Presenter> {
            fun getCountryCodesArray(): Array<String> // From strings.xml file
            fun getTelephonyManager(): TelephonyManager // Get telephony manager
            fun showDefaultCountryCode(code: String) // To show default country code selected at first
            fun showValidationError()
            fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
        }

        interface Presenter : BasePresenter {
            fun getDefaultCountryCode()
            fun submitMobileNumber(mobile: String)
            fun callForgotpasscode(mobile: String, countrycode: String, appData: AppData)
            fun resetpasscode(passcode: String, appData: AppData)
        }
    }
}