package com.guulpay.aboutus

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.aboutus.model.AboutUsResponse
import com.guulpay.aboutus.servicecall.AboutusRepository
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.others.Constants
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class AboutUsPresenter(private val viewSignup: AboutUSContract.View, private val aboutusRepository: AboutusRepository) : AboutUSContract.Presenter {

    private var disposable: Disposable? = null
    val TAG = "AboutUsPresenter"


    override fun start() {
        viewSignup.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        viewSignup.handleProgressAlert(false)
    }

    override fun callAboutUs() {
        if (!viewSignup.isNetworkAvailable()) {
            viewSignup.showNetworkUnavailableMsg()
            return
        }
        //appData.register_country_code=countrycode
        viewSignup.handleProgressAlert(true)
        disposable = aboutusRepository.callAboutUsApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    viewSignup.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.d("json_response", json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            viewSignup.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val aboutUsResponse = GsonBuilder().create().fromJson(json, AboutUsResponse::class.java)
                            viewSignup.setAboutusdata(aboutUsResponse.content.toString())
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (viewSignup.isActivityRunning()) {
                        viewSignup.handleProgressAlert(false)
                        if (viewSignup.isNetworkAvailable())
                            viewSignup.showSomeErrorOccurredMsg("Something went wrong")
                        else viewSignup.showNetworkUnavailableMsg()
                    }
                })


    }
}