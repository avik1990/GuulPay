package com.guulpay.countryCodes.models

import com.google.gson.annotations.SerializedName

data class CountryWiseBankModel(@SerializedName("name") var bankname: String, @SerializedName("code") var bankCode: String, @SerializedName("icons") var bankIcons: String/*, var code:String*/) {
}