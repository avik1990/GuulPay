package com.guulpay.enterOtp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OtpafterResponseSucess {

    @SerializedName("responseCode")
    @Expose
    private val responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    private val responseDetails: String? = null
    @SerializedName("otp")
    @Expose
    private val otp: Int? = null
}
