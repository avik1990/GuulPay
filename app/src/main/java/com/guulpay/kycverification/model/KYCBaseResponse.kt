package com.guulpay.kycverification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KYCBaseResponse {

    @SerializedName("response")
    @Expose
    var response: Response? = null

    inner class Response {

        @SerializedName("data")
        @Expose
        var data: String? = null
        @SerializedName("salt")
        @Expose
        var salt: String? = null

    }

}
