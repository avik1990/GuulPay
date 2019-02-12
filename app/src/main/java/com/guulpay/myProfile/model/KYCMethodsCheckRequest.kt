package com.guulpay.myProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KYCMethodsCheckRequest {

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

        @SerializedName("user_id")
        @Expose
        var userId: String? = null
        @SerializedName("country_id")
        @Expose
        var country_id: String? = null
    }


}
