package com.guulpay.operatorList

import android.content.Context
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.operatorList.models.OperatorModel
import com.guulpay.operatorList.servicecall.OperatorCodeRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class OperatorCodesPresenter(private val context: Context, private val view: OperatorCodesContract.View, private val fetchCountryCodeRepository: OperatorCodeRepository, private val appData: AppData) : OperatorCodesContract.Presenter {

    private var disposable: Disposable? = null
    val TAG = "OperatorCodesPresenter"

    init {
        //   fetchCountryCodesAsyncTask = FetchCountryCodesAsynTask()
    }

    override fun fetchOperators() {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = fetchCountryCodeRepository.callOperatorAPI(appData.register_country_code)
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
                            val signupresponsesucess = GsonBuilder().create().fromJson(json, OperatorModel::class.java)
                            signupresponsesucess.Operatorlist()
                            if (signupresponsesucess.operatorlist!!.size > 0)
                                view.operatorFetched(signupresponsesucess.operatorlist!!)
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
