package com.guulpay.guulex

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.guulex.model.ForexResponse
import com.guulpay.guulex.servicecall.ForexTransactionRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class GuulexPresenter(private val viewContactus: GuulexContract.View, private val aboutusRepository: ForexTransactionRepository, private val appData: AppData) : GuulexContract.Presenter {

    private var disposable: Disposable? = null
    val TAG = "GuulexFragment"

    override fun start() {
        viewContactus.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        viewContactus.handleProgressAlert(false)
    }

    override fun getForexData() {
        if (!viewContactus.isNetworkAvailable()) {
            viewContactus.showNetworkUnavailableMsg()
            return
        }

        viewContactus.handleProgressAlert(true)
        disposable = aboutusRepository.calltransactionAPI(appData)
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
                            val forexResponse = GsonBuilder().create().fromJson(json, ForexResponse::class.java)
                            viewContactus.inflateData(forexResponse.forexlist!!)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            viewContactus.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
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
}