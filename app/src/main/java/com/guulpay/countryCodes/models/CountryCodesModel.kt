package com.guulpay.countryCodes.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CountryCodesModel {

    @SerializedName("responseCode")
    @Expose
    val responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    val responseDetails: String? = null
    @SerializedName("prefixlist")
    @Expose
    val prefixlist: ArrayList<Prefixlist>? = null

    inner class Prefixlist {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("callprefix")
        @Expose
        var callprefix: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("currency_code")
        @Expose
        var currency_code: String? = null
    }
}