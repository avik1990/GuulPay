package com.guulpay.contactus.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ContactUsRequest {

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
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null
    }
}
