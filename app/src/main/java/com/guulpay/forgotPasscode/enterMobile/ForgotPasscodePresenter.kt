package com.guulpay.forgotPasscode.enterMobile

import android.util.Base64
import android.util.Log
import com.guulpay.forgotPasscode.servicecall.ForgotPasscodeRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class ForgotPasscodePresenter(viewForgotPwd: ForgotPasscodeContract.EnterMobile.View, private val signUpRepository: ForgotPasscodeRepository, private val appData: AppData) : ForgotPasscodeContract.EnterMobile.Presenter {


    var viewForgotPwd: ForgotPasscodeContract.EnterMobile.View
    private var disposable: Disposable? = null
    val TAG = "ForgotPasscodePresenter"

    init {
        this.viewForgotPwd = viewForgotPwd
        viewForgotPwd.setPresenter(this)
    }

    override fun submitMobileNumber(mobile: String) {
        /*if (mobile.trim().length == 0) {
            viewForgotPwd.showValidationError()
            return
        } else {
            viewForgotPwd.goToNextPage()
        }*/
        // viewForgotPwd.goToNextPage()
    }

    override fun start() {
        viewForgotPwd.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        viewForgotPwd.handleProgressAlert(false)
    }

    override fun getDefaultCountryCode() {
        var countryId = ""
        var countryZipCode = "358" // Default Finland country code is initialized

        val manager = viewForgotPwd.getTelephonyManager()
        countryId = manager.simCountryIso.toUpperCase()
        val countryArray = viewForgotPwd.getCountryCodesArray()

        for (i in countryArray.indices) {
            val tempArray = countryArray[i].split(",".toRegex())
            if (tempArray[1].equals(countryId)) {
                countryZipCode = tempArray[0]
                break
            }
        }
        viewForgotPwd.showDefaultCountryCode("+" + countryZipCode)
    }

    override fun callForgotpasscode(mobile: String, countrycode: String, appData: AppData) {
        if (!viewForgotPwd.isNetworkAvailable()) {
            viewForgotPwd.showNetworkUnavailableMsg()
            return
        }
        viewForgotPwd.handleProgressAlert(true)
        disposable = signUpRepository.forgotpasscodeApi(mobile, countrycode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    viewForgotPwd.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.e("Response_in_forget_pass", json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            viewForgotPwd.showSomeErrorOccurredMsg(obj.get("responseDetails").toString())
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            viewForgotPwd.goToNextPage()
                            //Log.d("Success", "Sucess")
                            // val signupresponsesucess = GsonBuilder().create().fromJson(json, SignUpResponseSucess::class.java)
                            /*if (viewForgotPwd.isActivityRunning()) {
                                viewForgotPwd.goToNextPage()
                            }*/
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (viewForgotPwd.isActivityRunning()) {
                        viewForgotPwd.handleProgressAlert(false)
                        if (viewForgotPwd.isNetworkAvailable())
                            viewForgotPwd.showSomeErrorOccurredMsg("Something went wrong")
                        else viewForgotPwd.showNetworkUnavailableMsg()
                    }
                })
    }

    override fun resetpasscode(passcode: String, appData: AppData) {
        if (!viewForgotPwd.isNetworkAvailable()) {
            viewForgotPwd.showNetworkUnavailableMsg()
            return
        }
        viewForgotPwd.handleProgressAlert(true)
        disposable = signUpRepository.resetpasscodeApi(passcode, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    viewForgotPwd.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.e("Response", json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            viewForgotPwd.showSomeErrorOccurredMsg(obj.get("responseDetails").toString())
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            viewForgotPwd.goToNextPage()
                            //Log.d("Success", "Sucess")
                            // val signupresponsesucess = GsonBuilder().create().fromJson(json, SignUpResponseSucess::class.java)
                            /*if (viewForgotPwd.isActivityRunning()) {
                                viewForgotPwd.goToNextPage()
                            }*/
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (viewForgotPwd.isActivityRunning()) {
                        viewForgotPwd.handleProgressAlert(false)
                        if (viewForgotPwd.isNetworkAvailable())
                            viewForgotPwd.showSomeErrorOccurredMsg("Something went wrong")
                        else viewForgotPwd.showNetworkUnavailableMsg()
                    }
                })
    }

}