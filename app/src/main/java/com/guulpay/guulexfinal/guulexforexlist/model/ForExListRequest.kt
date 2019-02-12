package com.guulpay.guulexfinal.guulexforexlist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForExListRequest {
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
        @SerializedName("base_currency")
        var base_currency = ""
    }
}