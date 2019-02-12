package com.guulpay.myProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EmailVerificationResponse {

    @SerializedName("response")
    @Expose
    var response: Response? = null


    inner class Response {

        @SerializedName("responseCode")
        @Expose
        var responseCode: Int? = null
        @SerializedName("responseDetails")
        @Expose
        var responseDetails: String? = null

    }

}
