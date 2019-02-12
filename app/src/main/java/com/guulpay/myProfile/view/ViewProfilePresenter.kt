package com.guulpay.myProfile.view

import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.guulpay.myProfile.model.EmailVerificationResponse
import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.myProfile.servicecall.MyProfileRepository
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class ViewProfilePresenter(private val view: ViewProfileContract.View, private val appData: AppData, private val myProfileRepository: MyProfileRepository) : ViewProfileContract.Presenter {

    val TAG = "ViewProfilePresenter"
    private var disposable: Disposable? = null

    init {
        view.setPresenter(this)
    }

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        view.handleProgressAlert(false)
    }

    override fun showUserDetails() {
        //view.showUserDetails()
    }


    override fun callUserDetailsApi(appData: AppData) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = myProfileRepository.callUserDetailsApi(appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val userDetailsResponse = GsonBuilder().create().fromJson(json, UserDetailsResponse::class.java)
                            Log.d("signupresponsesucess", userDetailsResponse.toString())
                            if (userDetailsResponse.user!!.is_kyc_verified!!.isEmpty()) {
                                appData.is_kyc_verified = "0"
                            } else {
                                appData.is_kyc_verified = userDetailsResponse.user!!.is_kyc_verified.toString()
                            }
                            view.showUserDetails(userDetailsResponse.user)
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
                            view.showSomeErrorOccurredMsg("Something went wrong")
                        else view.showNetworkUnavailableMsg()
                    }
                })
    }


    override fun callUSerEmailValidate(appData: AppData, email: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = myProfileRepository.callUSerEmailValidate(appData, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    Log.v("email_inside_disposable", email)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.v("json_disposable", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("object_response", obj.toString())
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            view.showToast("Click on the link sent to your email to verify")
                            // Toast.makeText(context,"Click on the Link to send to your mail for verify", Toast.LENGTH_LONG).show()
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            view.globalLogout()
                            // Toast.makeText(context,"Click on the Link to send to your mail for verify", Toast.LENGTH_LONG).show()
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