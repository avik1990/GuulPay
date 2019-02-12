package com.guulpay.guulexfinal.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForexCartBuyResponse {
    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("cart_id")
    @Expose
    var cart_id: String? = null
    @SerializedName("present_country_id")
    @Expose
    var present_country_id: String? = null
}