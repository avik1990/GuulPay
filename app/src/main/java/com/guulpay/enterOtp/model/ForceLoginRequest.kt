package com.guulpay.enterOtp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ForceLoginRequest {

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
        var phone = ""

        @SerializedName("device_type")
        var device_type = ""

        @SerializedName("device_token")
        var device_token = ""

        @SerializedName("otp")
        var otp = ""
    }
}
