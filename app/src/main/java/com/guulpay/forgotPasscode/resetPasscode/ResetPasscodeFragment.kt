package com.guulpay.forgotPasscode.resetPasscode

import android.os.Bundle
import android.support.v4.app.Fragment
import android.telephony.TelephonyManager
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.customUiComponents.PasscodeHandler
import com.guulpay.forgotPasscode.enterMobile.ForgotPasscodeContract
import com.guulpay.forgotPasscode.enterMobile.ForgotPasscodePresenter
import com.guulpay.forgotPasscode.servicecall.ForgotPasscodeRepositoryProvider
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.confirm_passcode_layout.*
import kotlinx.android.synthetic.main.enter_passcode_layout.*
import kotlinx.android.synthetic.main.entermobile_forgot_passcode_fragment.*
import kotlinx.android.synthetic.main.reset_passcode.*

class ResetPasscodeFragment : BaseFragment(), View.OnClickListener, ForgotPasscodeContract.EnterMobile.View, PasscodeHandler.PasscodeListener {

    var isPasscodeVisible: Boolean = false
    var isConfirmPasscodeVisible = false
    lateinit var forgotPasscodePresenter: ForgotPasscodePresenter
    var passcode: String = ""
    var passcode1: String = ""

    private val loader by lazy {
        LoaderDialog(context)
    }


    companion object {
        const val CLASS_NAME = "ResetPasscodeFragment"
        fun newInstance(): Fragment {
            return ResetPasscodeFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.reset_passcode
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        ForgotPasscodePresenter(this, ForgotPasscodeRepositoryProvider.getForgotPasscodeRepository(), AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)).start()
        /*tvResetPasscodeText.setCustomFont(context,context?.getString(R.string.quicksandMedium),true)
        tvEnterResetPwdTxt.setCustomFont(context,context?.getString(R.string.quicksandMedium),false)*/
        PasscodeHandler(etPasscode1, etPasscode2, etPasscode3, etPasscode4, this)
        PasscodeHandler(etConfirmPasscode1, etConfirmPasscode2, etConfirmPasscode3, etConfirmPasscode4, this)
    }

    override fun initListeners() {
        btSubmitResetPasscode.setOnClickListener(this)
        imgvwShowHidePwd.setOnClickListener(this)
        imgvwShowHideConfirmPwd.setOnClickListener(this)
    }

    override fun onResult(status: Boolean) {
        if (status) {

        }
    }

    override fun onClick(v: View?) {
        if (v == btSubmitResetPasscode) {
            if (etPasscode1.text.toString().isEmpty() || etPasscode2.text.toString().isEmpty() || etPasscode3.text.toString().isEmpty() || etPasscode4.text.toString().isEmpty() ||
                    etConfirmPasscode1.text.toString().isEmpty() || etConfirmPasscode2.text.toString().isEmpty() || etConfirmPasscode3.text.toString().isEmpty() || etConfirmPasscode4.text.toString().isEmpty()) {
                return
            } else {
                passcode = etPasscode1.text.toString() + "" + etPasscode2.text.toString() + "" + etPasscode3.text.toString() + "" + etPasscode4.text.toString()
                passcode1 = etConfirmPasscode1.text.toString() + "" + etConfirmPasscode2.text.toString() + "" + etConfirmPasscode3.text.toString() + "" + etConfirmPasscode4.text.toString()
                if (passcode.equals(passcode1)) {
                    forgotPasscodePresenter.resetpasscode(passcode, AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference))
                } else {
                    Utils.showSnackbar(llresetPassword, "Passcode doesn't matches", 3000)
                }
            }
            // activity?.finish()
        } else if (v == imgvwShowHidePwd) {
            if (isPasscodeVisible) {
                etPasscode1.transformationMethod = PasswordTransformationMethod()
                etPasscode2.transformationMethod = PasswordTransformationMethod()
                etPasscode3.transformationMethod = PasswordTransformationMethod()
                etPasscode4.transformationMethod = PasswordTransformationMethod()
                imgvwShowHidePwd.background = activity?.getDrawable(R.drawable.hide_icon)
            } else {
                etPasscode1.transformationMethod = null
                etPasscode2.transformationMethod = null
                etPasscode3.transformationMethod = null
                etPasscode4.transformationMethod = null
                imgvwShowHidePwd.background = activity?.getDrawable(R.drawable.view_icon)
            }
            isPasscodeVisible = !isPasscodeVisible

        } else if (v == imgvwShowHideConfirmPwd) {
            if (isConfirmPasscodeVisible) {
                etConfirmPasscode1.transformationMethod = PasswordTransformationMethod()
                etConfirmPasscode2.transformationMethod = PasswordTransformationMethod()
                etConfirmPasscode3.transformationMethod = PasswordTransformationMethod()
                etConfirmPasscode4.transformationMethod = PasswordTransformationMethod()
                imgvwShowHideConfirmPwd.background = activity?.getDrawable(R.drawable.hide_icon)
            } else {
                etConfirmPasscode1.transformationMethod = null
                etConfirmPasscode2.transformationMethod = null
                etConfirmPasscode3.transformationMethod = null
                etConfirmPasscode4.transformationMethod = null
                imgvwShowHideConfirmPwd.background = activity?.getDrawable(R.drawable.view_icon)
            }
            isConfirmPasscodeVisible = !isConfirmPasscodeVisible
        }
    }


    override fun getCountryCodesArray(): Array<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTelephonyManager(): TelephonyManager {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDefaultCountryCode(code: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showValidationError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }

    }

    override fun goToNextPage() {
        Utils.showToast(context,"Password successfully updated.")
        activity?.finish()
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: ForgotPasscodeContract.EnterMobile.Presenter) {
        forgotPasscodePresenter = presenter as ForgotPasscodePresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llresetPassword, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llForgotPwdParent, msg, 3000)
    }
    override fun globalLogout() {
     //   appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}
