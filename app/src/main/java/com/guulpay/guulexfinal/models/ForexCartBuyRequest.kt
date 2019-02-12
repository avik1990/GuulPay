package com.guulpay.guulexfinal.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForexCartBuyRequest {
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
        @SerializedName("type")
        var type: Int? = null
        @SerializedName("user_id")
        var user_id : String?= null
        @SerializedName("base_currency")
        var base_currency : String?= null
        @SerializedName("present_country_id")
        var present_country_id :String?= null
        @SerializedName("forex_data")
        var forex_data :ArrayList<ForexData>?= null


    }

    inner class ForexData{
        @SerializedName("forex_transaction_type_id")
        var forex_transaction_type_id: Int? = null
        @SerializedName("booking_currency")
        var booking_currency : String?= null
        @SerializedName("original_rate")
        var original_rate : String?= null
        @SerializedName("rate_with_admin_percent")
        var rate_with_admin_percent :String?= null
        @SerializedName("final_amount_normal_rate")
        var final_amount_normal_rate :String?= null
        @SerializedName("final_amount_commission_rate")
        var final_amount_commission_rate :String?= null
        @SerializedName("amount")
        var amount :String?= null
    }
}