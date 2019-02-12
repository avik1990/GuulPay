package com.guulpay.guulexfinal.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CalculateAmountRequest {
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
        @SerializedName("base_currency")
        var base_currency: String? = null
        @SerializedName("booking_currency")
        var booking_currency : String?= null
        @SerializedName("forex_ammount")
        var forex_ammount : String?= null
        @SerializedName("type")
        var type :String?= null

    }


}