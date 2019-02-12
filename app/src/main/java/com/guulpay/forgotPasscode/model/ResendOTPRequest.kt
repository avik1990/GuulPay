package com.guulpay.forgotPasscode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResendOTPRequest {
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
        @SerializedName("phone")
        @Expose
        public var phone: String? = null
    }

}