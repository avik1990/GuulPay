package com.guulpay.login

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.login.models.LoginResponseModel
import com.guulpay.login.serviceCall.LoginRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class LoginPresenter(private val view: LoginContract.View,
                     private val appData: AppData, private val loginRepository: LoginRepository) : LoginContract.Presenter {

    val TAG = "LoginPresenter"
    var isPasscodeVisible = false
    private var disposable: Disposable? = null

    init {
        view.setPresenter(this)
    }

    override fun start() {
        view.showPhoneNumberScreen()
        /* Checks whether the app is installed first time or not
        * If first time, the mobile number layout will be visible
        * Else, redirect to the passcode layout with Fingerprint */
        /* if (appData.isFirstTimeInstalled){
             view.showPhoneNumberScreen()
         }
         else{
             view.hidePhoneNumberScreen()
         }*/
    }

    override fun stop() {
        disposable?.dispose()
        view.handleProgressAlert(false)
    }

    override fun callLoginApi(isFingerPrintValidated: Boolean, phoneno: String, passcode_1: String, passcode_2: String, passcode_3: String, passcode_4: String) {
        Log.d("phoneno", phoneno)
        /* When normal login occurs on button click */
        if (!isFingerPrintValidated) {
            if (!view.isNetworkAvailable()) {
                view.showNetworkUnavailableMsg()
                return
            }

            if (passcode_1.trim().length == 0 && passcode_2.trim().length == 0 && passcode_3.trim().length == 0 && passcode_4.trim().length == 0) {
                view.passcodeValidationFailed()
                return
            }

            view.handleProgressAlert(true)

            val passcode = passcode_1 + "" + passcode_2 + "" + passcode_3 + "" + passcode_4
            appData.register_password = passcode
            appData.register_phone = phoneno
            disposable = loginRepository.callLoginApi(passcode, phoneno, appData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view.handleProgressAlert(false)
                        if (!it.response.data.isEmpty()) {
                            val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                            Log.d("Json_Stirng", json)
                            val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                            if (obj.get("responseCode").toString().equals("201")) {
                                view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            } else if (obj.get("responseCode").toString().equals("200")) {
                                // view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                                val loginresponsesucess = GsonBuilder().create().fromJson(json, LoginResponseModel::class.java)
                                Log.d("RawToken", loginresponsesucess.user!!.rememberToken.toString())
                                Log.v("profile_image_login",loginresponsesucess.user!!.mainimage.toString())

                                appData.register_phone = loginresponsesucess.user!!.phone.toString()
                                appData.register_email = loginresponsesucess.user!!.email.toString()
                                appData.user_id = loginresponsesucess.user!!.id.toString()
                                appData.remember_token = loginresponsesucess.user!!.rememberToken.toString()
                                appData.country_call_prifix = loginresponsesucess.user!!.countryCallPrifix.toString()
                                appData.register_name = loginresponsesucess.user!!.name.toString()
                                appData.register_country_code = loginresponsesucess.user!!.countryId.toString()
                                appData.register_countryName = loginresponsesucess.user!!.countryName.toString()
                                appData.register_currency_code = loginresponsesucess.user!!.currencyCode.toString()
                                appData.register_currency_name = loginresponsesucess.user!!.currencyName.toString()
                                appData.register_wallet_id = loginresponsesucess.user!!.walletId.toString()
                                appData.register_wallet_currency_id = loginresponsesucess.user!!.walletCurrencyId.toString()
                                appData.register_amount = loginresponsesucess.user!!.amount.toString()

                                appData.register_isEmailVerify = loginresponsesucess.user!!.isEmailVerify.toString()
                                appData.register_isPhoneVerify = loginresponsesucess.user!!.isPhoneVerify.toString()
                                appData.register_dateOfBirth = loginresponsesucess.user!!.dateOfBirth.toString()
                                //Saving image to local Edited on 5/02/2019 Rishabh
                                appData.profile_image=loginresponsesucess.user!!.mainimage.toString()
                                if (loginresponsesucess.user!!.is_kyc_verified.toString().isEmpty()) {
                                    appData.is_kyc_verified = "0"
                                } else {
                                    appData.is_kyc_verified = loginresponsesucess.user!!.is_kyc_verified.toString()
                                }

                                appData.isLoggedin = true
                                Log.d("SavedToken",appData.remember_token)
                                if (view.isActivityRunning()) {
                                    view.goToNextPage()
                                }
                            } else if (obj.get("responseCode").toString().equals("203")) {
                                view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                                if (view.isActivityRunning()) {
                                    view.gotoOtpScreen()
                                }
                            } else if (obj.get("responseCode").toString().equals("401")) {
                                view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            } else if (obj.get("responseCode").toString().equals("701")) {
                                view.globalLogout()
                            }
                        }
                    }, {
                        Log.e(TAG, it.toString())
                        if (view.isActivityRunning()) {
                            view.handleProgressAlert(false)
                            if (view.isNetworkAvailable())
                                view.showSomeErrorOccurredMsg(view.getContext().getString(R.string.someErrorOccurred))
                            else view.showNetworkUnavailableMsg()
                        }
                    })

        } else {
            if (!view.isNetworkAvailable()) {
                view.showNetworkUnavailableMsg()
                return
            }
            view.handleProgressAlert(true)
            val passcode = appData.register_password
            disposable = loginRepository.callLoginApi(passcode, phoneno, appData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view.handleProgressAlert(false)
                        if (!it.response.data.isEmpty()) {
                            val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                            Log.d("Json_Stirng_login", json)
                            val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                            if (obj.get("responseCode").toString().equals("201")) {
                                //appData.isLoggedin = false
                                view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            } else if (obj.get("responseCode").toString().equals("200")) {
                                val loginresponsesucess = GsonBuilder().create().fromJson(json, LoginResponseModel::class.java)
                                Log.d("RawToken", loginresponsesucess.user!!.rememberToken.toString())
                                Log.v("profile_image_login",loginresponsesucess.user!!.mainimage.toString())
                                appData.register_phone = loginresponsesucess.user!!.phone.toString()
                                appData.register_email = loginresponsesucess.user!!.email.toString()
                                appData.user_id = loginresponsesucess.user!!.id.toString()
                                appData.profile_image = loginresponsesucess.user!!.mainimage.toString()
                                appData.remember_token = loginresponsesucess.user!!.rememberToken.toString()
                                appData.country_call_prifix = loginresponsesucess.user!!.countryCallPrifix.toString()
                                appData.register_name = loginresponsesucess.user!!.name.toString()
                                appData.register_country_code = loginresponsesucess.user!!.countryId.toString()
                                appData.register_countryName = loginresponsesucess.user!!.countryName.toString()
                                appData.register_currency_code = loginresponsesucess.user!!.currencyCode.toString()
                                appData.register_currency_name = loginresponsesucess.user!!.currencyName.toString()
                                appData.register_wallet_id = loginresponsesucess.user!!.walletId.toString()
                                appData.register_wallet_currency_id = loginresponsesucess.user!!.walletCurrencyId.toString()
                                appData.register_amount = loginresponsesucess.user!!.amount.toString()
                                appData.register_isEmailVerify = loginresponsesucess.user!!.isEmailVerify.toString()
                                appData.register_isPhoneVerify = loginresponsesucess.user!!.isPhoneVerify.toString()
                                appData.register_dateOfBirth = loginresponsesucess.user!!.dateOfBirth.toString()
                                if (loginresponsesucess.user!!.is_kyc_verified.toString().isEmpty()) {
                                    appData.is_kyc_verified = "0"
                                } else {
                                    appData.is_kyc_verified = loginresponsesucess.user!!.is_kyc_verified.toString()
                                }
                                Log.d("SavedToken",appData.remember_token)
                                appData.isLoggedin = true
                                if (view.isActivityRunning()) {
                                    view.goToNextPage()
                                }
                            } else if (obj.get("responseCode").toString().equals("203")) {
                                view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                                if (view.isActivityRunning()) {
                                    view.gotoOtpScreen()
                                }
                            } else if (obj.get("responseCode").toString().equals("701")) {
                                view.globalLogout()
                            }
                        }
                    }, {
                        Log.e(TAG, it.toString())
                        if (view.isActivityRunning()) {
                            view.handleProgressAlert(false)
                            if (view.isNetworkAvailable())
                                view.showSomeErrorOccurredMsg(view.getContext().getString(R.string.someErrorOccurred))
                            else view.showNetworkUnavailableMsg()
                        }
                    })


        }
        /* Login through FingerPrint */
        /* if (view.isActivityRunning())
             view.goToNextPage()*/
    }

    override fun validatePhoneNumber(number: String) {
        if (number.trim().length == 0) {
            view.phoneNumberValidationFailed()
            return
        }
        // appData.isFirstTimeInstalled = true
        view.phoneNumberValidatedSuccessfully()
    }

    override fun showHidePasscode() {
        if (isPasscodeVisible) {
            view.hidePasscode()
        } else {
            view.showPasscode()
        }
        isPasscodeVisible = !isPasscodeVisible
    }

    override fun getDefaultCountryCode() {
        var countryId = ""
        var countryZipCode = "358" // Default Finland country code is initialized

        val manager = view.getTelephonyManager()
        countryId = manager.simCountryIso.toUpperCase()
        val countryArray = view.getCountryCodesArray()

        for (i in countryArray.indices) {
            val tempArray = countryArray[i].split(",".toRegex())
            if (tempArray[1].equals(countryId)) {
                countryZipCode = tempArray[0]
                break
            }
        }
        view.showDefaultCountryCode("+" + countryZipCode)
    }
}