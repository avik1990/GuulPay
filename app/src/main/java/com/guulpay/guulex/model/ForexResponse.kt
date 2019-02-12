package com.guulpay.guulex.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ForexResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("forexlist")
    @Expose
    var forexlist: ArrayList<Forexlist>? = null

    inner class Forexlist {

        @SerializedName("base_currency")
        @Expose
        var baseCurrency: String? = null
        @SerializedName("pair_currency")
        @Expose
        var pairCurrency: String? = null
        @SerializedName("selling_rate")
        @Expose
        var sellingRate: String? = null
        @SerializedName("buying_rate")
        @Expose
        var buyingRate: String? = null
        @SerializedName("country")
        @Expose
        var country: String? = null

    }

}
