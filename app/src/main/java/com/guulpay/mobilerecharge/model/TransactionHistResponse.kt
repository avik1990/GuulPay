package com.guulpay.mobilerecharge.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TransactionHistResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("rechargelist")
    @Expose
    var rechargelist: List<Rechargelist>? = null
    @SerializedName("totalRecord")
    @Expose
    var totalRecord: String? = null
    @SerializedName("currentPage")
    @Expose
    var currentPage: String? = null
    @SerializedName("perPage")
    @Expose
    var perPage: String? = null
    @SerializedName("totalPage")
    @Expose
    var totalPage: String? = null

    inner class Rechargelist {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("user_id")
        @Expose
        var userId: String? = null
        @SerializedName("mobile_recharge_call_prifix")
        @Expose
        var mobileRechargeCallPrifix: String? = null
        @SerializedName("mobile_recharge_phone_number")
        @Expose
        var mobileRechargePhoneNumber: String? = null
        @SerializedName("reciver_user_currency_code")
        @Expose
        var reciverUserCurrencyCode: String? = null
        @SerializedName("sender_credit_currency_code")
        @Expose
        var senderCreditCurrencyCode: String? = null
        @SerializedName("mobile_recharge_amount")
        @Expose
        var mobileRechargeAmount: String? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("logo")
        @Expose
        var logo: String? = null
        @SerializedName("currency_symbol")
        @Expose
        var currency_symbol: String? = null
    }

}
