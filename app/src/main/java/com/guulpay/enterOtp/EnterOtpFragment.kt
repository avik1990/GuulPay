package com.guulpay.enterOtp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.customUiComponents.PasscodeHandler
import com.guulpay.dashboard.DashboardMainContentFragment
import com.guulpay.enterOtp.servicecall.EnterOTPRepositoryProvider
import com.guulpay.forgotPasscode.resetPasscode.ResetPasscodeFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.login.LoginActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.signup.SignupActivity
import kotlinx.android.synthetic.main.enter_otp_fragment.*
import kotlinx.android.synthetic.main.enter_otp_layout.*

/* It is called from EnterMobileForgotPasscode and SignUp fragments */
class EnterOtpFragment : BaseFragment(), View.OnClickListener, PasscodeHandler.PasscodeListener, EnterOtpContract.View {
    override fun globalLogout() {
        appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }

    lateinit var enterOtpPresenter: EnterOtpPresenter
    var fromWhichFragment: String = ""
    var appData: AppData? = null

    companion object {
        const val CLASS_NAME = "EnterOtpFragment"
        /* Fragment name specifies from which fragment it is calling */
        /* And , to get mobile number from previous screen/page */
        fun newInstance(fromWhichFragment: String, mobileNumber: String): Fragment {
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME, fromWhichFragment)
            bundle.putString(Constants.Keys.KEY_MOBILE_NUMBER, mobileNumber)
            // bundle.putString(Constants.Keys.KEY_FROM_AFTER_LOGINRESPONSE_203, mobileNumber)
            val fragment = EnterOtpFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.enter_otp_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        PasscodeHandler(etOtp1, etOtp2, etOtp3, etOtp4, this)
    }

    override fun initListeners() {
        EnterOtpPresenter(this, EnterOTPRepositoryProvider.getotpafterrigistrationRepository(), appData!!).start()
        btSubmitEnterOtp.setOnClickListener(this)
        tvResendOtp.setOnClickListener(this)

        fromWhichFragment = arguments!!.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        Log.d("fromWhichFragment", fromWhichFragment)
        /* if (!fromWhichFragment.equals("EnterMobileForgotPasscodeFragment")) {
             enterOtpPresenter.otpafterrigistration()
         }*/
        if (fromWhichFragment.equals(SignupActivity.CLASS_NAME)) {
            enterOtpPresenter.otpafterrigistration()
        } else if (fromWhichFragment.equals(LoginActivity.CLASS_NAME_203)) {
            enterOtpPresenter.otpafterlogin()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        enterOtpPresenter.stop()
    }

    override fun onClick(v: View?) {
        if (v == btSubmitEnterOtp) {
            if (etOtp1.text.toString().isEmpty()) {
                return
            }
            if (etOtp2.text.toString().isEmpty()) {
                return
            }
            if (etOtp3.text.toString().isEmpty()) {
                return
            }
            if (etOtp4.text.toString().isEmpty()) {
                return
            }
            enterOtpPresenter.validateOtp(etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString())
        } else if (v == tvResendOtp) {
            //Utils.showToast(context, appData!!.register_phone)
            enterOtpPresenter.resendOtp(appData!!.register_phone)
            enterOtpPresenter.startTimer()
        }
    }

    override fun onResult(status: Boolean) {
        if (status) {
            btSubmitEnterOtp.performClick()
        }
    }

    /* EnterOtp View callbacks */
    override fun getArgument(): Bundle? {
        return arguments
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

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: EnterOtpContract.Presenter) {
        enterOtpPresenter = presenter as EnterOtpPresenter
        enterOtpPresenter.startTimer()
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llEnterOtpFrag, getString(R.string.networkUnavailable), 3000)

    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llEnterOtpFrag, msg, 3000)
    }

    override fun gotoResetPasscodeScreen() {
        Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, ResetPasscodeFragment.newInstance(), R.id.flFragmentContainerHome, false, ResetPasscodeFragment.CLASS_NAME)
    }

    override fun gotoDashboardScreen() {
        Utils.hideKeyboard(activity)
        val intent = Intent(activity, DashboardHomeActivity::class.java)
        intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, DashboardMainContentFragment.CLASS_NAME)
        startActivity(intent)
        activity?.finishAffinity()
    }

    override fun updateTimerCount(count: String) {
        tvTimer.text = count
    }

    override fun updateSecsText(text: String) {
        tvSecs.text = text
    }

    override fun enableResendOTP() {
        llDidntReceiveOtpResend.alpha = 1.0F
        tvResendOtp.isEnabled = true
    }

    override fun disableResendOTP() {
        llDidntReceiveOtpResend.alpha = 0.4F
        tvResendOtp.isEnabled = false
    }

    override fun showMobileNumberOnScreen(number: String) {
        tvOtpSentMsg.text = String.format(getString(R.string.otpSentMsg, number))
    }

}
