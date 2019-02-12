package com.guulpay.signup.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SignUpResponseSucess {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("emailorphone")
    @Expose
    var emailorphone: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("device_type")
    @Expose
    var deviceType: String? = null
    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null
    @SerializedName("country_code")
    @Expose
    var countryCode: String? = null
}
