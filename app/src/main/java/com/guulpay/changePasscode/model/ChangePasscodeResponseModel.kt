package com.guulpay.changePasscode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ChangePasscodeResponseModel {

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
}



