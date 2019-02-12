package com.guulpay.kycverification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KYCUrlGeneratorResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null

}
