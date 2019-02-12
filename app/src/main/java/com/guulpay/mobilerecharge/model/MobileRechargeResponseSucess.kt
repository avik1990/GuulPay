package com.guulpay.mobilerecharge.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MobileRechargeResponseSucess {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("wallet_balance")
    @Expose
    var walletBalance: String? = null
    @SerializedName("transaction_id")
    @Expose
    var transactionId: String? = null
}
