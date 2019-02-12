package com.guulpay.mobilerecharge.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MobileRechargeRequest {

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
        var user_id = ""

       /* @SerializedName("operator_id")
        var operator_id = ""*/

        @SerializedName("phone")
        var phone = ""

       /* @SerializedName("country_id")
        var country_id = ""*/

        @SerializedName("amount")
        var amount = ""

        @SerializedName("use_wallet")
        var isWalletselected = ""

        @SerializedName("call_prefix")
        var call_prefix = ""

        @SerializedName("currency_code")
        var currency_code = ""

        @SerializedName("wallet_id")
        var wallet_id = ""

        @SerializedName("base_currency_code")
        var base_currency_code = ""

    }

    /*{
        "call_prefix" : "91",   dfcdfdfd
        "currency_code": "INR",sdsada
        "phone" : "9475524306",asdsada
        "amount": "500",dsfs
        "use_wallet" : "1",czcz
        "user_id" : "132",zxczc
        "wallet_id" : "3"
    }*/


}
