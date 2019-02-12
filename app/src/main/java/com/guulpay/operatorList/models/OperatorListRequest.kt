package com.guulpay.operatorList.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OperatorListRequest {

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
        @SerializedName("country_id")
        var country_id = ""
    }
}
