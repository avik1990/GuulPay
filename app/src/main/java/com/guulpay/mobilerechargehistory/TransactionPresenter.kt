package com.guulpay.mobilerechargehistory

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.mobilerecharge.MobileRechargeContract
import com.guulpay.mobilerecharge.model.TransactionHistResponse
import com.guulpay.mobilerecharge.servicecall.MobileRechargeRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class TransactionPresenter(private val view: MobileRechargeContract.TransactionContract.View, private val mobileRechargeRepository: MobileRechargeRepository, private val appData: AppData) : MobileRechargeContract.TransactionContract.Presenter {

    var currencyCode: String = ""
    private var disposable: Disposable? = null
    val TAG = "TransactionPresenter"
    private var rechargelist: MutableList<TransactionHistResponse.Rechargelist> = ArrayList()
    var OFFSET_COUNT = 15

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        view.handleProgressAlert(false)
    }

    override fun callMobilerechargelistApi(appData: AppData, flag: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        Log.d("ArraySize", "" + rechargelist!!.size)
        disposable = mobileRechargeRepository.callRechargehist(rechargelist!!.size, OFFSET_COUNT, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.e(TAG, json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val transactionHistResponse = GsonBuilder().create().fromJson(json, TransactionHistResponse::class.java)
                            rechargelist.addAll(transactionHistResponse.rechargelist!!)
                            view.onMobilerechargelistFetched(rechargelist!!)
                            Log.d("ArraySizeAfter", "" + rechargelist!!.size)
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