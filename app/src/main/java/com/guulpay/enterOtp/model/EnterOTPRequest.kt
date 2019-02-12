package com.guulpay.enterOtp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EnterOTPRequest {

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
        @SerializedName("emailorphone")
        var emailorphone = ""

        @SerializedName("password")
        var password = ""

        @SerializedName("device_type")
        var device_type = ""

        @SerializedName("device_token")
        var device_token = ""

        @SerializedName("country_code")
        var country_code = ""

        @SerializedName("otp")
        var otp = ""
    }
}
