package com.guulpay.forgotPasscode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ForgotPasswordRequest {

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
    }
}
