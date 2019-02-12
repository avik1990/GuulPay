package com.guulpay.customUiComponents

import android.content.Context
import com.guulpay.others.APIService
import com.guulpay.others.LoaderDialog
import io.reactivex.disposables.Disposable

class GetCountryIdByCodeHandler(context: Context, private val countrycode: String) {

    private var disposable: Disposable? = null
    val TAG = "CodeHandler"
    private val apiService: APIService? = null

    private val loader by lazy {
        LoaderDialog(context)
    }


    init {

    }

    interface GetCountryId {
        fun onResult(countrycode: String)
    }


    /*fun fetchCountryCodes() {
        loader.show()
        disposable = apiService!!.callCountryCodeAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //Log.e(TAG, it.response.data.toString() +"\n"+it.response.salt.toString())
                    loader.hide()
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.e(TAG, json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            //view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            Toast.makeText(context)
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
    }*/


}