package com.guulpay.mobilerecharge.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MsisdninfoResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("operatorlist")
    @Expose
    var operatorlist: Operatorlist? = null

    inner class Operatorlist {

        @SerializedName("country")
        @Expose
        var country: String? = null
        @SerializedName("countryid")
        @Expose
        var countryid: String? = null
        @SerializedName("operator")
        @Expose
        var operator: String? = null
        @SerializedName("operatorid")
        @Expose
        var operatorid: String? = null
        @SerializedName("product_list")
        @Expose
        var productList: ArrayList<String>? = null
    }

}
