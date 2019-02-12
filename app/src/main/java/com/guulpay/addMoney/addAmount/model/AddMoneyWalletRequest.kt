package com.guulpay.addMoney.addAmount.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AddMoneyWalletRequest {

    @SerializedName("request")
    @Expose
    var request: Request? = null

    inner class Request {

        @SerializedName("data")
        @Expose
        var data: String? = null
        @SerializedName("salt")
        @Expose
        var salt: String? = null
    }

    inner class RequestVariables {
        @SerializedName("user_id")
        @Expose
        var userId: String? = null
        @SerializedName("wallet_id")
        @Expose
        var walletId: String? = null
        @SerializedName("amount")
        @Expose
        var amount: String? = null
        @SerializedName("currency_code")
        @Expose
        var currencyCode: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("phone")
        @Expose
        var phone: String? = null
        @SerializedName("call_prefix")
        @Expose
        var callPrefix: String? = null
        @SerializedName("is_kyc_verified")
        @Expose
        var is_kyc_verified: String? = null
    }
}
