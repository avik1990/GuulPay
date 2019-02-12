package com.guulpay.guulexfinal.guulexforexlist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForExListResponse {
    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("forexlist")
    @Expose
    var forexlist: ArrayList<ForExList>? = null


    inner class ForExList{


        @SerializedName("base_currency")
        @Expose
        private val baseCurrency: String? = null
        @SerializedName("pair_currency")
        @Expose
        val pairCurrency: String? = null
        @SerializedName("selling_rate")
        @Expose
        val sellingRate: String? = null
        @SerializedName("buying_rate")
        @Expose
        val buyingRate: String? = null
        @SerializedName("country")
        @Expose
        private val country: String? = null
        @SerializedName("currency_symbol")
        @Expose
        private val currencySymbol: String? = null
    }
}