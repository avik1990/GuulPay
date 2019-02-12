package com.guulpay.guulexfinal

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.guulexfinal.guulexforexlist.model.ForExListResponse
import com.guulpay.guulexfinal.models.CalculateAmountResponse
import com.guulpay.guulexfinal.models.CurrencyListResponse
import com.guulpay.guulexfinal.models.ForexCartBuyRequest
import com.guulpay.guulexfinal.models.ForexCartBuyResponse
import com.guulpay.guulexfinal.servicecall.CurrencyListRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class CurrencyCodePresenter(private val view: CurrencyListContract.View,
                           private val appData: AppData,
                           private val currencyListRepository: CurrencyListRepository) : CurrencyListContract.Presenter{


    private var disposable: Disposable? = null
    val TAG = "CurrencyCode"

    override fun callCurrencyListApi(base_currency: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }

        view.handleProgressAlert(true)
        disposable = currencyListRepository.callCurrencyListApi(base_currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())

                    //Comment this when done 25-01-2019

                   //  view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("object",obj.toString())
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val currencyCodeSuucess = GsonBuilder().create().fromJson(json, CurrencyListResponse::class.java)
                            //view.showSomeErrorOccurredMsg(currencyCodeSuucess.responseDetails.toString())
                            val currency_list=obj.get("currency_list")
                            Log.v("currency_list",currency_list.toString())
                            view.getCurrencyCodeArray(currencyCodeSuucess.currencyList!!)
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



    override fun callForExApi(base_currency: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }

         view.handleProgressAlert(true)
        disposable = currencyListRepository.callForExAPI(base_currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json_forex", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("object_forex",obj.toString())
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val forExListSuccess = GsonBuilder().create().fromJson(json, ForExListResponse::class.java)
                            //view.showSomeErrorOccurredMsg(currencyCodeSuucess.responseDetails.toString())
//                            val currency_list=obj.get("currency_list")
//                            Log.v("forex_list",currency_list.toString())
                            Log.v("forexsuccess",forExListSuccess.toString())

                            view.getForExListArray(forExListSuccess.forexlist!!)

                            /* val json_array_data = obj.getJSONArray("currency_list")
                             Log.v("currency_list_jarray",json_array_data.toString())
                             for (i in 0..json_array_data!!.length() - 1) {
                                 //val categories = FoodCategoryObject()
                                 val country_name = json_array_data.getJSONObject(i).getString("country")
                                 Log.v("country_name",country_name)

                             }
                             Log.v("CurrencyCodeSuccess",currencyCodeSuucess.toString())*/


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


//    "base_currency" : "EUR",
//    //    "booking_currency" : "USD",
////    "forex_ammount" : "100",
////    "type" : "0"
    override fun callCalculateAmountApi(base_currency: String, booking_currency: String, forex_ammount: String, type: String) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }

        view.handleProgressAlert(true)

        disposable = currencyListRepository.callCalculateAmountApi(base_currency,booking_currency,forex_ammount,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)

                    Log.v("base_currency",base_currency)
                    Log.v("booking_currency",booking_currency)
                    Log.v("forex_amount",forex_ammount.toString())
                    Log.v("type",type)

                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json_cal_amount", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("object_cal_amount",obj.toString())
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val calAmountSuccess = GsonBuilder().create().fromJson(json, CalculateAmountResponse::class.java)

                            Log.v("calAmountSuccess",calAmountSuccess.toString())
                            view.getCalCulatedAmount(calAmountSuccess)


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


    override fun callForexCartBuyApi(type: Int, user_id: String, base_currency: String, parent_country_id: String, forex_data: ArrayList<ForexCartBuyRequest.ForexData>) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }

        view.handleProgressAlert(true)
        disposable = currencyListRepository.callForexBuyCartApi(type,user_id,base_currency,parent_country_id,forex_data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json_forex", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("object_forex",obj.toString())
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val forExListBuyCartSuccess = GsonBuilder().create().fromJson(json, ForexCartBuyResponse::class.java)

                          //  Log.v("forexsuccess",forExListSuccess.toString())

                          //  view.getForExListArray(forExListSuccess.forexlist!!)

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
        view.setPresenter(this)
    }

    override fun stop() {
       //TODO implemented later
    }

}


