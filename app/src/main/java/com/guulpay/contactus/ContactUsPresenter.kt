package com.guulpay.contactus

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.contactus.model.CoontactUsResponse
import com.guulpay.contactus.servicecall.ContactusRepository
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class ContactUsPresenter(private val viewContactus: ContactUSContract.View, private val aboutusRepository: ContactusRepository, private val appData: AppData) : ContactUSContract.Presenter {

    private var disposable: Disposable? = null
    val TAG = "ContactUsPresenter"

    override fun start() {
        viewContactus.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        viewContactus.handleProgressAlert(false)
    }

    override fun callContactUsAPi(name: String, email: String, message: String) {
        if (!viewContactus.isNetworkAvailable()) {
            viewContactus.showNetworkUnavailableMsg()
            return
        }

        viewContactus.handleProgressAlert(true)
        disposable = aboutusRepository.callContactUsApi(name, email, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    viewContactus.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.d("json_response", json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            viewContactus.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            viewContactus.goToNextPage()
                            //val aboutUsResponse = GsonBuilder().create().fromJson(json, CoontactUsResponse::class.java)
                            //viewContactus.setAboutusdata(aboutUsResponse.content.toString())
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            viewContactus.globalLogout()
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (viewContactus.isActivityRunning()) {
                        viewContactus.handleProgressAlert(false)
                        if (viewContactus.isNetworkAvailable())
                            viewContactus.showSomeErrorOccurredMsg("Something went wrong")
                        else viewContactus.showNetworkUnavailableMsg()
                    }
                })
    }

    override fun checkFieldsValidation(name: String, email: String, message: String) {
        viewContactus.disableButton()

        if (name.trim().length <= 1) {
            viewContactus.fieldsValidationFailed("Please enter name")
            return
        }

        if (email.trim().length <= 1) {
            viewContactus.fieldsValidationFailed("Please enter email")
            return
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                viewContactus.fieldsValidationFailed("Please enter valid email")
                return
            }
        }

        if (message.trim().length <= 1) {
            viewContactus.fieldsValidationFailed("Please enter message")
            return
        }
        viewContactus.enableButton()

    }

}