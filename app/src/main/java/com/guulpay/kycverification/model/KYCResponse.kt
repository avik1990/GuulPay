package com.guulpay.kycverification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class KYCResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null


}
