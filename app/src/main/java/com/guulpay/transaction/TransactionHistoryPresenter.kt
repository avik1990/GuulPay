package com.guulpay.transaction

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.guulexfinal.CurrencyListContract
import com.guulpay.guulexfinal.models.CurrencyListResponse
import com.guulpay.guulexfinal.servicecall.CurrencyListRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.transaction.model.TransactionHistoryResponse
import com.guulpay.transaction.servicecall.TransactionHistoryRepository
import com.guulpay.transaction.servicecall.TransactionHistoryRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class TransactionHistoryPresenter (private val view: TransactionHistoryContract.View,
                                   private val appData: AppData,
                                   private val transactionHistoryRepository: TransactionHistoryRepository) : TransactionHistoryContract.Presenter {
    private var disposable: Disposable? = null
    val TAG = "TransactionHistory"

    override fun callTranactionHistoryAPI(user_id: String, start: Int, offset: Int, transaction_type_id: String, transaction_from_date: String, transaction_to_date: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }

        view.handleProgressAlert(true)
        disposable = transactionHistoryRepository.callTransactionHistoryAPI(user_id ,start,offset,transaction_type_id,transaction_from_date,transaction_to_date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())

                    //Comment this when done 25-01-2019

                      view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json_thistory", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("object",obj.toString())
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val transactionHistorySuucess = GsonBuilder().create().fromJson(json, TransactionHistoryResponse::class.java)
                          //  Log.v("data_res_trans",json)
                            //view.showSomeErrorOccurredMsg(currencyCodeSuucess.responseDetails.toString())
                          //  val currency_list=obj.get("currency_list")
                         //   Log.v("currency_list",currency_list.toString())
                                //view.getCurrencyCodeArray(currencyCodeSuucess.currencyList!!)
                            //  view.getCurrencySpinner(currencyCodeSuucess.currencyList!!)
                        }
                    }

                }, {
                    Log.e(TAG, it.toString())
                    if (view.isActivityRunning()) {
                        // view.handleProgressAlert(false)
                        if (view.isNetworkAvailable())
                            view.showSomeErrorOccurredMsg("Something went wrong")
                        else view.showNetworkUnavailableMsg()
                    }
                })


    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}