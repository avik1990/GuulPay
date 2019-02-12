package com.guulpay.guulexfinal.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.guulpay.mobilerecharge.model.MsisdninfoResponse
import com.guulpay.guulexfinal.models.CurrencyListResponse.CurrencyList



class CurrencyListResponse{

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("currency_list")
    @Expose
    var currencyList: ArrayList<CurrencyList>? = null


    inner class CurrencyList{

        @SerializedName("country")
        @Expose
        var country: String? = null
        @SerializedName("symbol")
        @Expose
        var symbol: String? = null
        @SerializedName("currencykey")
        @Expose
        var currencykey: String? = null
    }

}