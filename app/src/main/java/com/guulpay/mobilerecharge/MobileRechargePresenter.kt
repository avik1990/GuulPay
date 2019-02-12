package com.guulpay.mobilerecharge

import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import com.guulpay.countryCodes.models.CountryCodesModel
import com.guulpay.mobilerecharge.model.MobileRechargeResponseSucess
import com.guulpay.mobilerecharge.model.MsisdninfoResponse
import com.guulpay.mobilerecharge.servicecall.MobileRechargeRepository
import com.guulpay.others.AppData
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.payments.fetchContacts.ContactsModel
import com.guulpay.payments.fetchContacts.GetContactsInteractor
import com.guulpay.payments.fetchContacts.GetContactsInteractorImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class MobileRechargePresenter(private val view: MobileRechargeContract.EnterPhoneContract.View, private val contactsInteractorImpl: GetContactsInteractorImpl, private val mobileRechargeRepository: MobileRechargeRepository, private val appData: AppData) : MobileRechargeContract.EnterPhoneContract.Presenter, GetContactsInteractor.OnFinishedListener {


    var countryZipCode: String = ""
    var countryId: String = ""
    var currencyCode: String = ""

    override fun getDefaultCountryCode() {
        var countryId = ""
        countryZipCode = "358"// Default Finland country code is initialized

        val manager = view.getTelephonyManager()
        countryId = manager.simCountryIso.toUpperCase()
        val countryArray = view.getCountryCodesArray()

        for (i in countryArray.indices) {
            val tempArray = countryArray[i].split(",".toRegex())
            if (tempArray[1].equals(countryId)) {
                countryZipCode = tempArray[0]
                break
            }
        }
        view.showDefaultCountryCode("+" + countryZipCode)
    }

    override fun fetchContacts() {
        if (view.isFragmentAlive()) {
            view.handleProgressAlert(true)
            contactsInteractorImpl.getContactsList(this)
        }
    }

    private var disposable: Disposable? = null
    val TAG = "MobileRechargePresenter"

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        view.handleProgressAlert(false)
    }

    /* GetContactsInteractor.OnFinishedListener callback methods */
    override fun onFinished(list: ArrayList<ContactsModel>) {
        view.handleProgressAlert(false)
        view.onContactListFetched(list)
    }

    override fun callRechargeApi(user_id: String, recharege_currency_code: String, mobile_no: String, callprefix: String, amount: String, isWalletselected: String, appData: AppData) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }

        view.handleProgressAlert(true)
        disposable = mobileRechargeRepository.callRecharge(user_id, recharege_currency_code, mobile_no, callprefix, amount, isWalletselected, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("json", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val signupresponsesucess = GsonBuilder().create().fromJson(json, MobileRechargeResponseSucess::class.java)
                            view.showSomeErrorOccurredMsg(signupresponsesucess.responseDetails.toString())
                            appData.register_amount = signupresponsesucess.walletBalance.toString()
                            if (view.isActivityRunning()) {
                                view.goToNextPage()
                            }
                        } else if (obj.get("responseCode").toString().equals("102")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("203")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("101")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            view.globalLogout()
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

    override fun callCountrylistApi() {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = mobileRechargeRepository.callCountryCodeAPI()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.e(TAG, it.Response().data.toString() +"\n"+it.Response().salt.toString())
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val signupresponsesucess = GsonBuilder().create().fromJson(json, CountryCodesModel::class.java)
                            if (signupresponsesucess.prefixlist!!.size > 0) {
                                for (i in signupresponsesucess.prefixlist.indices) {
                                    if (signupresponsesucess.prefixlist[i].callprefix!!.equals(countryZipCode)) {
                                        countryId = signupresponsesucess.prefixlist[i].id.toString()
                                        currencyCode = signupresponsesucess.prefixlist[i].currency_code.toString()
                                        break
                                    }
                                }
                                view.getCountryIdfromApi(countryId, currencyCode)
                            }
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

    override fun callMsisdninfoeApi(call_prefix: String, phone: String, appData: AppData) {
        if (!view.isNetworkAvailable()) {
            view.showNetworkUnavailableMsg()
            return
        }
        view.handleProgressAlert(true)
        disposable = mobileRechargeRepository.callMsisdninfo(call_prefix, phone, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.e(TAG, json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            try {
                                view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            } catch (e: Exception) {

                            }
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val msisdninfoResponse = GsonBuilder().create().fromJson(json, MsisdninfoResponse::class.java)
                            view.getTopUpAmts(msisdninfoResponse.operatorlist!!)
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