package com.guulpay.enterOtp

import android.os.Bundle
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface EnterOtpContract {

    interface View : BaseView<Presenter> {
        fun updateSecsText(text: String)
        fun updateTimerCount(count: String)
        fun enableResendOTP()
        fun disableResendOTP()
        fun gotoResetPasscodeScreen()
        fun gotoDashboardScreen()
        fun getArgument(): Bundle?
        fun showMobileNumberOnScreen(number: String)
        fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
    }

    interface Presenter : BasePresenter {
        fun validateOtp(otp: String)
        fun resendOtp(phoneno: String)
        fun stopTimer()
        fun startTimer()
        fun otpafterrigistration()
        fun loginafterrigistration(jsonString: String)
        fun VerifyOTP(jsonString: String)
        fun forceloginApicall(otp: String)
        fun otpafterlogin()
    }
}