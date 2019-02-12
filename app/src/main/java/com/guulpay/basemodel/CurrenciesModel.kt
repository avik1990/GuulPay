package com.guulpay.basemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrenciesModel {

    @SerializedName("cc")
    @Expose
    var cc: String? = null
    @SerializedName("symbol")
    @Expose
    var symbol: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}



