package com.guulpay.signup.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SignUpResponse201 {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null



}
