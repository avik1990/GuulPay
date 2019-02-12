package com.guulpay.signup

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.enterOtp.EnterOtpFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.signup.serviceCall.SignUpRepositoryProvider
import com.guulpay.termsAndConditions.TermsAndConditionsFragment
import kotlinx.android.synthetic.main.signup_activity.*

class SignupActivity : BaseActivity(), View.OnClickListener, SignupContract.View {


    lateinit var signupPresenter: SignupPresenter
    var isChecked = false
    var isTyping = false
    var country_code_id: String = ""
    var appData: AppData? = null

    private val loader by lazy {
        LoaderDialog(this)
    }

    companion object {
        val CLASS_NAME = "SignupActivity"
    }

    override fun initListeners() {
        tvCountryCode.setOnClickListener(this)
        btSignUp.setOnClickListener(this)
        tvSignInHere.setOnClickListener(this)

        chckbx.setOnCheckedChangeListener { compoundButton, isChecked ->
            isTyping = false
            this.isChecked = isChecked
            signupPresenter.checkFieldsValidation(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                    etPasscode.text.toString(), etConfirmPasscode.text.toString(), isChecked, tvCountryCode.text.toString())
        }

        CustomTextWatcher(etName, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                signupPresenter.checkFieldsValidation(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                        etPasscode.text.toString(), etConfirmPasscode.text.toString(), isChecked, tvCountryCode.text.toString())
            }
        })
        CustomTextWatcher(etMobile, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                signupPresenter.checkFieldsValidation(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                        etPasscode.text.toString(), etConfirmPasscode.text.toString(), isChecked, tvCountryCode.text.toString())
            }
        })
        CustomTextWatcher(etEmail, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                signupPresenter.checkFieldsValidation(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                        etPasscode.text.toString(), etConfirmPasscode.text.toString(), isChecked, tvCountryCode.text.toString())
            }
        })
        CustomTextWatcher(etPasscode, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                signupPresenter.checkFieldsValidation(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                        etPasscode.text.toString(), etConfirmPasscode.text.toString(), isChecked, tvCountryCode.text.toString())
            }
        })
        CustomTextWatcher(etConfirmPasscode, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                signupPresenter.checkFieldsValidation(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                        etPasscode.text.toString(), etConfirmPasscode.text.toString(), isChecked, tvCountryCode.text.toString())
            }
        })
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    override fun initResources() {
        appData = AppData(this, Constants.Keys._KeyCryptoPreference)
        SignupPresenter(this, appData!!, SignUpRepositoryProvider.getSignUpRepository()).start()

        val textPrivacyPolicyTC = "By signing up agree to our <b><font color=#348C96> " + getString(R.string.termsCondition) +
                "</font></b>" + " & " + "<b><font color=#348C96> " + getString(R.string.privacyPolicy) + "</font></b>"
        val textSignIn = getString(R.string.alreadyHaveAccount) + " <b><font color=#348C96> " + getString(R.string.signIn) + "</font></b>" + " here"

        /* To handle click on 'Sign In' text only */
        /*val clickableSpanSignIn = object : ClickableSpan() {
            override fun onClick(textView: View) {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        val ssSignIn = SpannableString(Html.fromHtml(textSignIn))
        ssSignIn.setSpan(clickableSpanSignIn, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)*/

        /* To handle click on 'Terms & Conditions' text only */
        val clickableSpanPrivacyPolicy = object : ClickableSpan() {
            override fun onClick(textView: View) {
                signupPresenter.checkTermsConditions()
                //    Utils.showSnackbar(llSignupParent, "Terms & Conditions : In progress", 3000)
                val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, TermsAndConditionsFragment.CLASS_NAME)
                startActivity(intent)
            }
        }
        val ssPrivacyPolicyTC = SpannableString(Html.fromHtml(textPrivacyPolicyTC))
        ssPrivacyPolicyTC.setSpan(clickableSpanPrivacyPolicy, 27, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        /* To handle click on 'Privacy Policy' text only */
        val clickableSpanTermsConditions = object : ClickableSpan() {
            override fun onClick(textView: View) {
                signupPresenter.goToPrivacyPolicy()
                Utils.showSnackbar(llSignupParent, "Coming Soon", 3000)
            }
        }
        ssPrivacyPolicyTC.setSpan(clickableSpanTermsConditions, 48, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvSignInHere.setText(Html.fromHtml(textSignIn))
        tvSignInHere.setMovementMethod(LinkMovementMethod.getInstance())
        tvPrivacyPolicyTC.setText(ssPrivacyPolicyTC)
        tvPrivacyPolicyTC.setMovementMethod(LinkMovementMethod.getInstance())
    }

    override fun getLayout(): Int {
        return R.layout.signup_activity
    }

    override fun onClick(v: View?) {
        if (v == btSignUp) {
            if (!country_code_id.isEmpty()) {
                signupPresenter.callsignUpApi(etName.text.toString(), etMobile.text.toString(), etEmail.text.toString(),
                        etPasscode.text.toString(), etConfirmPasscode.text.toString(), country_code_id, appData!!.save_devicetoken, "3")
            } else {
                Utils.showSnackbar(llSignupParent, getString(R.string.select_country_code), 3000)
            }
        } else if (v == tvSignInHere) {
            finish()
        } else if (v == tvCountryCode) {
            val intent = Intent(this, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
            countryCode?.let { tvCountryCode.text = it }
            val countryCodeID = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)
            countryCode?.let { country_code_id = countryCodeID!! }
        }
    }

    /* SignupContract.View callabck methods */
    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
        val number = tvCountryCode.text.toString() + " " + etMobile.text
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, EnterOtpFragment.CLASS_NAME)
        intent.putExtra(Constants.Keys.KEY_FROM_WHICH_FRAGMENT_CALLING, SignupActivity.CLASS_NAME)
        intent.putExtra(Constants.Keys.KEY_MOBILE_NUMBER, number)
        startActivity(intent)
        finish()
    }

    override fun isFragmentAlive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContext(): Context {
        return this
    }

    override fun isActivityRunning(): Boolean {
        return isActivityVisible
    }

    override fun setPresenter(presenter: SignupContract.Presenter) {
        signupPresenter = presenter as SignupPresenter
        signupPresenter.getDefaultCountryCode()
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(this)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llSignupParent, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llSignupParent, msg, 3000)
    }

    override fun getCountryCodesArray(): Array<String> {
        return resources.getStringArray(R.array.countryCodes)
    }

    override fun getTelephonyManager(): TelephonyManager {
        return getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun showDefaultCountryCode(code: String) {
        code?.let { tvCountryCode.text = it }
        signupPresenter.callCountrylistApi()
    }

    override fun fieldsValidationFailed(msg: String) {
        if (!isTyping)
            Utils.showSnackbar(llSignupParent, msg, 3000)
    }

    override fun enableButton() {
        btSignUp.isEnabled = true
        btSignUp.alpha = 1.0F
    }

    override fun disableButton() {
        btSignUp.isEnabled = false
        btSignUp.alpha = 0.5F
    }

    override fun getCountryIdfromApi(countryid: String) {
        country_code_id = countryid
        Log.d("countryIdFirstTime", countryid)
    }

    override fun globalLogout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
