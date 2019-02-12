package com.guulpay.enterOtp

import android.os.Handler
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.enterOtp.servicecall.EnterOTPRepository
import com.guulpay.forgotPasscode.enterMobile.EnterMobileForgotPasscodeFragment
import com.guulpay.login.LoginActivity
import com.guulpay.login.models.LoginResponseModel
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.signup.SignupActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class EnterOtpPresenter(private val view: EnterOtpContract.View, private val enterOTPRepository: EnterOTPRepository, private val appData: AppData) : EnterOtpContract.Presenter {


    var fragmentName: String? = ""
    var mobileNumber: String? = ""
    var handler: Handler
    private var disposable: Disposable? = null
    val TAG = "EnterOtpPresenter"

    companion object {
        var counter = 60
    }

    init {
        handler = Handler()
        fragmentName = view.getArgument()?.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        mobileNumber = view.getArgument()?.getString(Constants.Keys.KEY_MOBILE_NUMBER)
        Log.d("FragmentName", fragmentName)
    }

    override fun validateOtp(otp: String) {
        Log.d("FragmentName", fragmentName)
        if (fragmentName.equals(EnterMobileForgotPasscodeFragment.CLASS_NAME)) {
            VerifyOTP(otp)
        } else if (fragmentName.equals(SignupActivity.CLASS_NAME)) {
            loginafterrigistration(otp)
        } else if (fragmentName.equals(LoginActivity.CLASS_NAME)) {
            VerifyOTP(otp)
        } else if (fragmentName.equals(LoginActivity.CLASS_NAME_203)) {
            forceloginApicall(otp)
        }
    }

    override fun start() {
        mobileNumber?.let { view.showMobileNumberOnScreen(it) }
        view.setPresenter(this)
    }

    override fun stop() {
        counter = 60
        stopTimer()
        disposable?.dispose()
        view.handleProgressAlert(false)
    }

    override fun stopTimer() {
        handler.removeCallbacks(runnable)
    }

    override fun startTimer() {
        handler.postDelayed(runnable, 1000)
    }

    var runnable: Runnable = Runnable {
        if (counter > 0 && view.isFragmentAlive() && view.isActivityRunning()) {
            counter = counter - 1
            view.disableResendOTP()
            if (counter < 10) {
                view.updateTimerCount("0" + counter.toString())
            } else {
                view.updateTimerCount(counter.toString())
            }

            if (counter < 2) view.updateSecsText("second")
            else view.updateSecsText("seconds")
            startTimer()
        } else if (counter == 0) {
            stop()
            view.enableResendOTP()
        }
    }

    override fun otpafterrigistration() {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        // if (view.isFragmentAlive()) {
        disposable = enterOTPRepository.getotpafterrigistrationRepositoryApi(appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            //val signupresponsesucess = GsonBuilder().create().fromJson(json, EnterOTPResponseSucess::class.java)
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            /*if (view.isActivityRunning()) {
                                view.goToNextPage()
                            }*/
                        } else if (obj.get("responseCode").toString().equals("403")) {
                            view.showSomeErrorOccurredMsg("The Email OR Phone field is required")
                        } else if (obj.get("responseCode").toString().equals("408")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (view.isActivityRunning()) {
                        view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                            view.showSomeErrorOccurredMsg("Something went wrong")
                        else view.showNetworkUnavailableMsg()
                    }
                })
        //}

    }


    override fun loginafterrigistration(otp: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = enterOTPRepository.loginafterrigistration(otp, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val loginresponsesucess = GsonBuilder().create().fromJson(json, LoginResponseModel::class.java)
                            appData.register_phone = loginresponsesucess.user!!.phone.toString()
                            appData.remember_token = loginresponsesucess.user!!.rememberToken.toString()
                            appData.register_email = loginresponsesucess.user!!.email.toString()
                            appData.user_id = loginresponsesucess.user!!.id.toString()
                            appData.profile_image = loginresponsesucess.user!!.mainimage.toString()
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
                            appData.isLoggedin = true
                            if (view.isActivityRunning()) {
                                view.gotoDashboardScreen()
                            }
                            Log.e("RawTokenfterr", loginresponsesucess.user!!.rememberToken.toString())

                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("203")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (view.isActivityRunning()) {
                        view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                        //view.showSomeErrorOccurredMsg(view.getContext().getString(R.string.someErrorOccurred))
                        else view.showNetworkUnavailableMsg()
                    }
                })
    }


    override fun VerifyOTP(otp: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = enterOTPRepository.verifyOTP(otp, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            if (fragmentName.equals(LoginActivity.CLASS_NAME)) {
                                if (view.isActivityRunning()) {
                                    forceloginApicall(otp)
                                }
                            } else {
                                if (view.isActivityRunning()) {
                                    view.gotoResetPasscodeScreen()
                                }
                            }
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg("The phone field is required")
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (view.isActivityRunning()) {
                        view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                        //view.showSomeErrorOccurredMsg(view.getContext().getString(R.string.someErrorOccurred))
                        else view.showNetworkUnavailableMsg()
                    }
                })
    }


    override fun resendOtp(phoneno: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = enterOTPRepository.resendOTP(phoneno)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg("The phone field is required")
                        }
                    }
                }, {
                    if (view.isActivityRunning()) {
                        view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                        //view.showSomeErrorOccurredMsg(view.getContext().getString(R.string.someErrorOccurred))
                        else view.showNetworkUnavailableMsg()
                    }
                })
    }


    override fun forceloginApicall(otp: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = enterOTPRepository.forceafterrigistration(otp, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val loginresponsesucess = GsonBuilder().create().fromJson(json, LoginResponseModel::class.java)
                            appData.register_phone = loginresponsesucess.user!!.phone.toString()
                            appData.remember_token = loginresponsesucess.user!!.rememberToken.toString()
                            appData.register_email = loginresponsesucess.user!!.email.toString()
                            appData.user_id = loginresponsesucess.user!!.id.toString()
                            appData.profile_image = loginresponsesucess.user!!.mainimage.toString()
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
                            Log.e("RawTokenfterr", loginresponsesucess.user!!.rememberToken.toString())
                            if (loginresponsesucess.user!!.is_kyc_verified.toString().isEmpty()) {
                                appData.is_kyc_verified = "0"
                            } else {
                                appData.is_kyc_verified = loginresponsesucess.user!!.is_kyc_verified.toString()
                            }

                            appData.isLoggedin = true
                            if (view.isActivityRunning()) {
                                view.gotoDashboardScreen()
                            }
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }
                    }
                }, {
                    Log.e(TAG, it.toString())
                    if (view.isActivityRunning()) {
                        view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                        //view.showSomeErrorOccurredMsg(view.getContext().getString(R.string.someErrorOccurred))
                        else view.showNetworkUnavailableMsg()
                    }
                })
    }


    override fun otpafterlogin() {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        // if (view.isFragmentAlive()) {
        disposable = enterOTPRepository.callotpafterloginApi(appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            //val signupresponsesucess = GsonBuilder().create().fromJson(json, EnterOTPResponseSucess::class.java)
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            /*if (view.isActivityRunning()) {
                                view.goToNextPage()
                            }*/
                        } else if (obj.get("responseCode").toString().equals("403")) {
                            view.showSomeErrorOccurredMsg("The Email OR Phone field is required")
                        } else if (obj.get("responseCode").toString().equals("408")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (view.isActivityRunning()) {
                        view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                            view.showSomeErrorOccurredMsg("Something went wrong")
                        else view.showNetworkUnavailableMsg()
                    }
                })
    }
}