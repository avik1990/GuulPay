package com.guulpay.forgotPasscode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreatePasswordRequest {
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
        @SerializedName("password")
        @Expose
        public var password: String? = null
        @SerializedName("confirm_password")
        @Expose
        public var confirmPassword: String? = null
    }

}