package com.guulpay.mobilerecharge.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TransactionRequest {

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

        @SerializedName("start")
        var start = ""

        @SerializedName("offset")
        var offset = ""
    }

}
