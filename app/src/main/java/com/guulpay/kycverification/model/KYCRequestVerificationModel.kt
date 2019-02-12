package com.guulpay.kycverification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class KYCRequestVerificationModel {
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
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("signicate_method")
        @Expose
        var signicate_method: String? = null
        @SerializedName("device_type")
        @Expose
        var device_type: String? = null
    }

}



