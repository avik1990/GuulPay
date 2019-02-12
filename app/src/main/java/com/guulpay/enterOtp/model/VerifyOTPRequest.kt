package com.guulpay.enterOtp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VerifyOTPRequest {

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

        @SerializedName("country_code")
        var country_code = ""

        @SerializedName("otp")
        var otp = ""
    }

    inner class RequestVariablesOrpAfterLogin {
        @SerializedName("emailorphone")
        var emailorphone = ""
    }
}
