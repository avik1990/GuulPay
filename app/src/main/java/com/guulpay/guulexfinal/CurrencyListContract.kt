package com.guulpay.guulexfinal

import com.guulpay.guulexfinal.guulexforexlist.model.ForExListResponse
import com.guulpay.guulexfinal.models.CalculateAmountResponse
import com.guulpay.guulexfinal.models.CurrencyListResponse
import com.guulpay.guulexfinal.models.ForexCartBuyRequest
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface CurrencyListContract {

    interface View : BaseView<Presenter> {
       // fun getContext(): Context
       fun handleProgressAlert(showingStatus: Boolean)
        fun getCurrencyCodeArray(currencyList:ArrayList<CurrencyListResponse.CurrencyList>)

        fun getForExListArray(forExList: ArrayList<ForExListResponse.ForExList>)

        fun getCalCulatedAmount(calAmount:CalculateAmountResponse)


    }

    interface Presenter : BasePresenter {
        fun callCurrencyListApi(base_currency: String)
        fun callForExApi(base_currency: String)
        fun callCalculateAmountApi(base_currency: String, booking_currency:String, forex_amount: String, type:String)
       // fun handleProgressAlert(showingStatus: Boolean)

        fun callForexCartBuyApi(type:Int,user_id: String,base_currency: String,parent_country_id:String,forex_data:ArrayList<ForexCartBuyRequest.ForexData>)
    }

}