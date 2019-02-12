package com.guulpay.enterOtp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OtpafterrigistrationRequest {

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

    inner class EnterOTPRequest {
        @SerializedName("emailorphone")
        var emailorphone = ""

        @SerializedName("country_code")
        var country_code = ""

        @SerializedName("device_type")
        var device_type = ""

        override fun toString(): String {
            return "EnterOTPRequest(emailorphone='$emailorphone', country_code='$country_code')"
        }


    }
}
