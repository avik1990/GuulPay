package com.guulpay.mobilerecharge

import android.telephony.TelephonyManager
import com.guulpay.mobilerecharge.model.MsisdninfoResponse
import com.guulpay.mobilerecharge.model.TransactionHistResponse
import com.guulpay.others.AppData
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView
import com.guulpay.payments.fetchContacts.ContactsModel

interface MobileRechargeContract {

    /* For enter mobile number screen */
    interface EnterPhoneContract {
        interface View : BaseView<Presenter> {
            fun onContactListFetched(list: ArrayList<ContactsModel>)
            fun getCountryIdfromApi(country_id: String, currency_code: String)
            fun showDefaultCountryCode(country_code: String)
            fun getCountryCodesArray(): Array<String> // From strings.xml file
            fun getTelephonyManager(): TelephonyManager
            fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
            fun getTopUpAmts(msisdninfoResponse: MsisdninfoResponse.Operatorlist)

        }

        interface Presenter : BasePresenter {
            fun fetchContacts()
            fun getDefaultCountryCode()
            fun callCountrylistApi()
            fun callMsisdninfoeApi(call_prefix: String, phone: String, appData: AppData)
            fun callRechargeApi(user_id: String, operator_id: String, mobile_no: String, country_id: String, amount: String, isWalletselected: String, appData: AppData)
        }
    }


    /* For mobile history */
    interface TransactionContract {
        interface View : BaseView<Presenter> {
            fun onMobilerechargelistFetched(list: MutableList<TransactionHistResponse.Rechargelist>)
            fun handleProgressAlert(showingStatus: Boolean) // true --> show, false --> dismiss
            fun callPagination()

        }

        interface Presenter : BasePresenter {
            fun callMobilerechargelistApi(appData: AppData, flag: String)
        }
    }
}