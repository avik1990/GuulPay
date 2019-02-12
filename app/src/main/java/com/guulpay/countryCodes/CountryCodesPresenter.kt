package com.guulpay.countryCodes

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.countryCodes.models.CountryCodesModel
import com.guulpay.countryCodes.servicecall.FetchCountryCodeRepository
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class CountryCodesPresenter(private val context: Context, private val view: CountryCodesContract.View, private val fetchCountryCodeRepository: FetchCountryCodeRepository) : CountryCodesContract.Presenter {

    private var disposable: Disposable? = null
    val TAG = "CountryCodesPresenter"

    init {
        //   fetchCountryCodesAsyncTask = FetchCountryCodesAsynTask()
    }

    override fun fetchCountryCodes() {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = fetchCountryCodeRepository.callCountryCodeAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //Log.e(TAG, it.response.data.toString() +"\n"+it.response.salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.e(TAG, json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val signupresponsesucess = GsonBuilder().create().fromJson(json, CountryCodesModel::class.java)
                            if (signupresponsesucess.prefixlist!!.size > 0)
                                view.countryCodesFetched(signupresponsesucess.prefixlist!!)
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

    override fun start() {
        view.setPresenter(this)
        //jsonAsString = loadJSONFromAsset()
    }

    override fun stop() {
        disposable?.dispose()
        view.handleProgressAlert(false)
        /* if (!fetchCountryCodesAsyncTask.isCancelled)
             fetchCountryCodesAsyncTask.cancel(true)*/
    }
}
