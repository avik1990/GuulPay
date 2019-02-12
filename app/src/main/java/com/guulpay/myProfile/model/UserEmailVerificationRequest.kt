package com.guulpay.myProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserEmailVerificationRequest {

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
    }


}
