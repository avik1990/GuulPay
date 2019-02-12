package com.guulpay.login

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.text.Html
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.customUiComponents.PasscodeHandler
import com.guulpay.dashboard.DashboardMainContentFragment
import com.guulpay.enterOtp.EnterOtpFragment
import com.guulpay.forgotPasscode.enterMobile.EnterMobileForgotPasscodeFragment
import com.guulpay.login.serviceCall.LoginRepositoryProvider
import com.guulpay.others.*
import com.guulpay.signup.SignupActivity
import kotlinx.android.synthetic.main.enter_passcode_layout.*
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : BaseActivity(), View.OnClickListener, LoginContract.View {


    companion object {
        val CLASS_NAME = "LoginActivity"
        val CLASS_NAME_203 = "CLASS_NAME_203"
    }

    val TAG = "LoginActivity"
    lateinit var animLeftOut: Animation
    lateinit var animRightIn: Animation
    lateinit var loginPresenter: LoginPresenter
    var isFingerPrintValidated = false
    lateinit var appdata: AppData

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun onDestroy() {
        loginPresenter.stop()
        super.onDestroy()
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    /* All resources will be initialized here */
    override fun initResources() {
        val locale: String
        appdata = AppData(this, Constants.Keys._KeyCryptoPreference)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().locales.get(0).getCountry()
        } else {
            locale = getResources().getConfiguration().locale.getCountry()
        }

        // jsonAsString = loadJSONFromAsset()
        initAnimation()
        /* Set visibility & background dynamically, since it's used in another layout */
        llPasscode.visibility = View.GONE

        LoginPresenter(this, AppData(this, Constants.Keys._KeyCryptoPreference), LoginRepositoryProvider.getLoginRepository()).start()
        /* Set text color of Sign Up only */
        val textSignUpUpper = getString(R.string.dontHaveAnAccount) + " <b><font color=#348C96> " + getString(R.string.signUp) + " </font></b>" + " here"
        val textSignUpLower = getString(R.string.dontHaveAnAccount) + " <b><font color=#348C96> " + getString(R.string.signUp) + " </font></b>" + " here"
        tvDontHaveAnAccountUpperPart.setText(Html.fromHtml(textSignUpUpper))
        // tvDontHaveAnAccountLowerPart.setText(Html.fromHtml(textSignUpLower))
        Utils.hideKeyboardOnOutsideTouch(llLoginParent, this)
    }


    private fun initAnimation() {
        animLeftOut = AnimationUtils.loadAnimation(this, R.anim.exit_to_left)
        animRightIn = AnimationUtils.loadAnimation(this, R.anim.enter_from_right)
    }

    private fun checkForFingerPrint() {
        CheckFingerPrint().checkFingerPrint(this, object : CheckFingerPrint.FingerPrintListener {
            override fun isFingerPrintSupported(status: Boolean) {
                if (status) {
                    if (appdata.isLoggedin && appdata.isFingerPrint) {
                        llLowerLayout.visibility = View.VISIBLE
                        tvDontHaveAnAccountLowerPart.visibility = View.GONE
                        tvDontHaveAnAccountUpperPart.visibility = View.GONE
                    } else {
                        llLowerLayout.visibility = View.GONE
                        tvDontHaveAnAccountLowerPart.visibility = View.GONE
                        tvDontHaveAnAccountUpperPart.visibility = View.VISIBLE
                    }
                } else {
                    llLowerLayout.visibility = View.GONE
                    /*  tvDontHaveAnAccountLowerPart.visibility = View.GONE
                      tvDontHaveAnAccountUpperPart.visibility = View.VISIBLE*/
                }
            }

            override fun onSuccess() {
                isFingerPrintValidated = true
                // loginPresenter.callLoginApi(isFingerPrintValidated, "", "", "", "", "")
                if (appdata.isLoggedin && isActivityVisible && appdata.isFingerPrint) {
                    loginPresenter.callLoginApi(isFingerPrintValidated, appdata.register_phone, etPasscode1.text.toString(), etPasscode2.text.toString(),
                            etPasscode3.text.toString(), etPasscode4.text.toString())
                }
            }

            override fun onError(msg: String) {
                Utils.showSnackbar(llLoginParent, msg, 5000)
                isFingerPrintValidated = false
            }

        })
    }

    /* All listeners will be registered here */
    override fun initListeners() {
        tvCountryCode.setOnClickListener(this)
        imgvwShowHidePwd.setOnClickListener(this)
        tvForgotPwd.setOnClickListener(this)
        btNext.setOnClickListener(this)
        tvDontHaveAnAccountUpperPart.setOnClickListener(this)
        tvDontHaveAnAccountLowerPart.setOnClickListener(this)

        if (appdata.isLoggedin) {
            hidePhoneNumberScreen()
            tvDontHaveAnAccountUpperPart.visibility = View.GONE
        } else {
            appdata!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
            tvDontHaveAnAccountUpperPart.visibility = View.VISIBLE
        }

        PasscodeHandler(etPasscode1, etPasscode2, etPasscode3, etPasscode4, object : PasscodeHandler.PasscodeListener {
            override fun onResult(status: Boolean) {
                if (status) {
                    if (appdata.isLoggedin) {
                        loginPresenter.callLoginApi(isFingerPrintValidated, appdata!!.register_phone, etPasscode1.text.toString(), etPasscode2.text.toString(),
                                etPasscode3.text.toString(), etPasscode4.text.toString())
                    } else {
                        loginPresenter.callLoginApi(isFingerPrintValidated, etPhoneNumber.text.toString(), etPasscode1.text.toString(), etPasscode2.text.toString(),
                                etPasscode3.text.toString(), etPasscode4.text.toString())
                    }
                }
            }
        })

        CustomTextWatcher(etPhoneNumber, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length > 6) {
                    btNext.isEnabled = true
                    btNext.alpha = 1.0F
                } else {
                    btNext.isEnabled = false
                    btNext.alpha = 0.5F
                }
            }
        })
    }

    /* Returns the layout of the specific activity */
    override fun getLayout(): Int {
        return R.layout.login_activity
    }

    /* OnClick handlers */
    override fun onClick(v: View?) {
        if (v == imgvwShowHidePwd) {
            loginPresenter.showHidePasscode()
        } else if (v == btNext) {
            Utils.hideKeyboard(this)
            Log.e("PhoneNo", etPhoneNumber.text.toString())
            loginPresenter.validatePhoneNumber(etPhoneNumber.text.toString())
        } else if (v == tvForgotPwd) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, EnterMobileForgotPasscodeFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == tvDontHaveAnAccountUpperPart) {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        } else if (v == tvDontHaveAnAccountLowerPart) {
            tvDontHaveAnAccountUpperPart.performClick()
        } else if (v == tvCountryCode) {
            val intent = Intent(this, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
            //startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY_ID)
        }
    }

    override fun gotoOtpScreen() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, EnterOtpFragment.CLASS_NAME)
        intent.putExtra(Constants.Keys.KEY_FROM_WHICH_FRAGMENT_CALLING, LoginActivity.CLASS_NAME_203)
        intent.putExtra(Constants.Keys.KEY_MOBILE_NUMBER, etPhoneNumber.text.toString())
        startActivity(intent)
    }


    /* To get selected country codes from CountryCodesListActivity */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
            countryCode?.let { tvCountryCode.text = countryCode }
            val countryCodeID = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)
            countryCode?.let { Log.d("CountryCodeID", countryCodeID) }
        }
    }

    /* Presenter callback methods */
    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }

    }

    /* Returns Context */
    override fun getContext(): AppCompatActivity {
        return this@LoginActivity
    }

    override fun isActivityRunning(): Boolean {
        return isActivityVisible
    }

    override fun isFragmentAlive(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        // Required only for fragments not activity
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        loginPresenter = presenter as LoginPresenter
        loginPresenter.getDefaultCountryCode()
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llLoginParent, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llLoginParent, msg, 3000)
    }

    override fun goToNextPage() {
        Utils.hideKeyboard(this)
        val intent = Intent(this@LoginActivity, DashboardHomeActivity::class.java)
        intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, DashboardMainContentFragment.CLASS_NAME)
        startActivity(intent)
        finish()
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(this@LoginActivity)
    }

    override fun phoneNumberValidatedSuccessfully() {
        llPhoneLogin.startAnimation(animLeftOut)
        llPhoneLogin.visibility = View.GONE
        btNext.visibility = View.GONE

        llPasscode.startAnimation(animRightIn)
        tvForgotPwd.visibility = View.VISIBLE
        tvPasscodeText.visibility = View.VISIBLE
        llPasscode.visibility = View.VISIBLE

        if (appdata.isLoggedin && isActivityVisible) {
            checkForFingerPrint()
        }
    }

    override fun showPhoneNumberScreen() {
        llPhoneLogin.visibility = View.VISIBLE
        btNext.visibility = View.VISIBLE

        tvPasscodeText.visibility = View.GONE
        tvForgotPwd.visibility = View.GONE
        llPasscode.visibility = View.GONE

        llLowerLayout.visibility = View.GONE
        tvDontHaveAnAccountLowerPart.visibility = View.GONE
        tvDontHaveAnAccountUpperPart.visibility = View.GONE
    }

    override fun hidePhoneNumberScreen() {
        Utils.hideKeyboard(this)

        llPhoneLogin.visibility = View.GONE
        btNext.visibility = View.GONE

        tvPasscodeText.visibility = View.VISIBLE
        tvForgotPwd.visibility = View.VISIBLE
        llPasscode.visibility = View.VISIBLE

        /* Check finger print validation */
        if (appdata.isLoggedin) {
            checkForFingerPrint()
        }
    }

    override fun showPasscode() {
        etPasscode1.transformationMethod = null
        etPasscode2.transformationMethod = null
        etPasscode3.transformationMethod = null
        etPasscode4.transformationMethod = null
        imgvwShowHidePwd.background = getDrawable(R.drawable.view_icon)
    }

    override fun hidePasscode() {
        etPasscode1.transformationMethod = PasswordTransformationMethod()
        etPasscode2.transformationMethod = PasswordTransformationMethod()
        etPasscode3.transformationMethod = PasswordTransformationMethod()
        etPasscode4.transformationMethod = PasswordTransformationMethod()
        imgvwShowHidePwd.background = getDrawable(R.drawable.hide_icon)
    }

    override fun phoneNumberValidationFailed() {
        Utils.showSnackbar(llLoginParent, getString(R.string.enterMobileNumber), 3000)
    }

    override fun passcodeValidationFailed() {
        Utils.showSnackbar(llLoginParent, getString(R.string.enterPasscode), 3000)
    }

    override fun showDefaultCountryCode(code: String) {
        code?.let { tvCountryCode.text = code }
    }

    override fun getTelephonyManager(): TelephonyManager {
        return getSystemService(TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun getCountryCodesArray(): Array<String> {
        return resources.getStringArray(R.array.countryCodes)
    }

    override fun globalLogout() {
        appdata!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}