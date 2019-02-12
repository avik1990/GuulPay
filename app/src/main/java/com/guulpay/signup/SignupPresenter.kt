package com.guulpay.signup

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.countryCodes.models.CountryCodesModel
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.signup.model.SignUpResponseSucess
import com.guulpay.signup.serviceCall.SignUpRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class SignupPresenter(private val viewSignup: SignupContract.View, private val appData: AppData, private val signUpRepository: SignUpRepository) : SignupContract.Presenter {


    private var disposable: Disposable? = null
    val TAG = "SignupPresenter"
    var countryZipCode: String = ""
    var countryId: String = ""

    override fun checkFieldsValidation(name: String, mobile: String, email: String, passcode: String, confirmPasscode: String, isChecked: Boolean, countrycode: String) {
        viewSignup.disableButton()
        /* Field Order wise validation messages */
        if (name.trim().length <= 1) {
            viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.emptyNameField))
            return
        }
        if (mobile.trim().length == 0) {
            viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.emptyMobileField))
            return
        } else {
            if (mobile.trim().length < 7) {
                viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.invalidMobile))
                return
            }
        }
        if (email.trim().length <= 1) {
            viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.emptyEmailIdField))
            return
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.invalidEmail))
                return
            }
        }
        if (passcode.trim().length == 0) {
            viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.emptyPasscode))
            return
        } else {
            if (passcode.trim().length < 4) {
                viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.invalidPasscode))
                return
            }
        }
        if (confirmPasscode.trim().length == 0) {
            viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.emptyConfirmPasscode))
            return
        }
        if (!passcode.equals(confirmPasscode)) {
            viewSignup.fieldsValidationFailed(viewSignup.getContext().getString(R.string.passcodeMismatch))
            return
        }
        if (!isChecked) {
            return
        }

        viewSignup.enableButton()
    }





    override fun signupUser() {
        viewSignup.goToNextPage()
    }

    override fun getDefaultCountryCode() {
        var countryId = ""
        countryZipCode = "358"// Default Finland country code is initialized

        val manager = viewSignup.getTelephonyManager()
        countryId = manager.simCountryIso.toUpperCase()
        val countryArray = viewSignup.getCountryCodesArray()

        for (i in countryArray.indices) {
            val tempArray = countryArray[i].split(",".toRegex())
            if (tempArray[1].equals(countryId)) {
                countryZipCode = tempArray[0]
                break
            }
        }
        viewSignup.showDefaultCountryCode("+" + countryZipCode)
    }

    override fun goToPrivacyPolicy() {
    }

    override fun checkTermsConditions() {
    }

    override fun start() {
        viewSignup.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        viewSignup.handleProgressAlert(false)
    }

    override fun submitOtp() {
    }

    override fun callsignUpApi(name: String, mobile: String, email: String, passcode: String, c_passcode: String, countrycode: String, device_token: String, user_role: String) {
        if (!viewSignup.isNetworkAvailable()) {
            viewSignup.showNetworkUnavailableMsg()
            return
        }

        viewSignup.handleProgressAlert(true)
        disposable = signUpRepository.signUpApi(name, mobile, email, passcode, passcode, countrycode, device_token, "3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    viewSignup.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            viewSignup.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val signupresponsesucess = GsonBuilder().create().fromJson(json, SignUpResponseSucess::class.java)
                            appData.register_country_code = signupresponsesucess.countryCode.toString()
                            appData.register_device_token = device_token
                            appData.register_device_type = Constants.Keys.DEVICE_TYPE
                            appData.register_phone = mobile
                            appData.register_password = passcode
                            Log.v("signupdata",obj.toString())
                            if (viewSignup.isActivityRunning()) {
                                viewSignup.goToNextPage()
                            }
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (viewSignup.isActivityRunning()) {
                        viewSignup.handleProgressAlert(false)
                        if (viewSignup.isNetworkAvailable())
                            viewSignup.showSomeErrorOccurredMsg(viewSignup.getContext().getString(R.string.someErrorOccurred))
                        else viewSignup.showNetworkUnavailableMsg()
                    }
                })
    }

    override fun callCountrylistApi() {
        if (!viewSignup.isNetworkAvailable()) {
            viewSignup.showNetworkUnavailableMsg()
            return
        }
        //appData.register_country_code=countrycode
        viewSignup.handleProgressAlert(true)
        disposable = signUpRepository.callCountryCodeAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    viewSignup.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            viewSignup.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val signupresponsesucess = GsonBuilder().create().fromJson(json, CountryCodesModel::class.java)
                            if (signupresponsesucess.prefixlist!!.size > 0) {
                                for (i in signupresponsesucess.prefixlist.indices) {
                                    if (signupresponsesucess.prefixlist.get(i).callprefix!!.equals(countryZipCode)) {
                                        countryId= signupresponsesucess.prefixlist[i].id.toString()
                                        break
                                    }
                                }
                                viewSignup.getCountryIdfromApi(countryId)
                            }
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (viewSignup.isActivityRunning()) {
                        viewSignup.handleProgressAlert(false)
                        if (viewSignup.isNetworkAvailable())
                            viewSignup.showSomeErrorOccurredMsg(viewSignup.getContext().getString(R.string.someErrorOccurred))
                        else viewSignup.showNetworkUnavailableMsg()
                    }
                })
    }
}