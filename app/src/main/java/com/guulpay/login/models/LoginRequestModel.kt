package com.guulpay.login.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequestModel {
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
        @Expose
        var emailorphone: String? = null
        @SerializedName("password")
        @Expose
        var password: String? = null
        @SerializedName("device_type")
        @Expose
        var deviceType: String? = null
        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = null
    }


}



