package com.guulpay.transaction

import com.guulpay.guulexfinal.models.ForexCartBuyRequest
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface TransactionHistoryContract {
    interface View : BaseView<Presenter> {

        fun handleProgressAlert(showingStatus: Boolean)



    }

    interface Presenter : BasePresenter {

        fun callTranactionHistoryAPI(user_id: String,start:Int,offset:Int,transaction_type_id:String,transaction_from_date:String, transaction_to_date:String)
//        fun callCurrencyListApi(base_currency: String)
//        fun callForExApi(base_currency: String)
//        fun callCalculateAmountApi(base_currency: String, booking_currency:String, forex_amount: String, type:String)
//        // fun handleProgressAlert(showingStatus: Boolean)
//
//        fun callForexCartBuyApi(type:Int,user_id: String,base_currency: String,parent_country_id:String,forex_data:ArrayList<ForexCartBuyRequest.ForexData>)
    }
}