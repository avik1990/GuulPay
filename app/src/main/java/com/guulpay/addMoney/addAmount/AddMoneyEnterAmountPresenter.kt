package com.guulpay.addMoney.addAmount

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.addMoney.AddMoneyContract
import com.guulpay.addMoney.addAmount.model.AddMoneyResponse
import com.guulpay.addMoney.addAmount.servicecall.AddMoneyRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class AddMoneyEnterAmountPresenter(addMoneyView: AddMoneyContract.AddAmountContract.View, private val appData: AppData, private val addMoneyRepository: AddMoneyRepository) : AddMoneyContract.AddAmountContract.Presenter {

    var addMoneyView: AddMoneyContract.AddAmountContract.View
    var addAmount: Float
    private var disposable: Disposable? = null
    val TAG = "EnterAmountPresenter"

    init {
        addAmount = 0.00F
        this.addMoneyView = addMoneyView
        addMoneyView.setPresenter(this)
    }

    override fun addMoney() {
        if (!addMoneyView.isNetworkAvailable()) {
            addMoneyView.showNetworkUnavailableMsg()
            return
        }
        //   addMoneyView.goToNextPage()
    }

    override fun start() {
    }

    override fun stop() {

    }

    override fun addAmt100(amt: Float) {
        addAmount = amt + 100
        addMoneyView.calculatedAmt(addAmount.toString())
    }

    override fun addAmt500(amt: Float) {
        addAmount = amt + 500
        addMoneyView.calculatedAmt(addAmount.toString())
    }

    override fun addAmt1000(amt: Float) {
        addAmount = amt + 1000
        addMoneyView.calculatedAmt(addAmount.toString())
    }

    override fun clearAmt() {
        addAmount = 0.00F
    }

    override fun callWalletRecharegApi(amt: String, appData: AppData) {
        if (!addMoneyView.isNetworkAvailable()) {
            addMoneyView.showNetworkUnavailableMsg()
            return
        }
        addMoneyView.handleProgressAlert(true)
        // if (view.isFragmentAlive()) {
        disposable = addMoneyRepository.callWalletRechargeApi(amt, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    addMoneyView.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            addMoneyView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val response = GsonBuilder().create().fromJson(json, AddMoneyResponse::class.java)
                            appData.register_amount = response.newBalance.toString()
                            addMoneyView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            if (addMoneyView.isActivityRunning()) {
                                addMoneyView.goToNextPage()
                            }
                        } else if (obj.get("responseCode").toString().equals("403")) {
                            addMoneyView.showSomeErrorOccurredMsg("The Email Or Phone field is required")
                        } else if (obj.get("responseCode").toString().equals("408")) {
                            addMoneyView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }else if (obj.get("responseCode").toString().equals("203")) {
                            addMoneyView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }else if (obj.get("responseCode").toString().equals("401")) {
                            addMoneyView.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (addMoneyView.isActivityRunning()) {
                        addMoneyView.handleProgressAlert(false)
                        if (addMoneyView.isNetworkAvailable())
                            addMoneyView.showSomeErrorOccurredMsg("Something went wrong")
                        else addMoneyView.showNetworkUnavailableMsg()
                    }
                })
    }
}