package com.guulpay.addMoney.addAmount.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AddMoneyResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("newBalance")
    @Expose
    var newBalance: String? = null
    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String? = null


}