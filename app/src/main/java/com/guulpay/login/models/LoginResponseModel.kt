package com.guulpay.login.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class LoginResponseModel {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("user")
    @Expose
    var user: User? = null
    @SerializedName("response")
    @Expose
    var response: Response? = null


    inner class Response {

        @SerializedName("data")
        @Expose
        var data: Data? = null

    }

    inner class Data {

        @SerializedName("responseCode")
        @Expose
        var responseCode: String? = null
        @SerializedName("responseDetails")
        @Expose
        var responseDetails: String? = null
        @SerializedName("user")
        @Expose
        var user: User? = null

    }

    inner class User {
        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("phone")
        @Expose
        var phone: String? = null
        @SerializedName("gender")
        @Expose
        var gender: String? = null
        @SerializedName("date of birth")
        @Expose
        var dateOfBirth: String? = null
        @SerializedName("mainimage")
        @Expose
        var mainimage: String? = null
        @SerializedName("smallimage")
        @Expose
        var smallimage: String? = null
        @SerializedName("remember_token")
        @Expose
        var rememberToken: String? = null
        @SerializedName("type")
        @Expose
        var type: String? = null
        @SerializedName("is_email_verify")
        @Expose
        var isEmailVerify: String? = null
        @SerializedName("is_phone_verify")
        @Expose
        var isPhoneVerify: String? = null
        @SerializedName("country_id")
        @Expose
        var countryId: String? = null
        @SerializedName("country_call_prifix")
        @Expose
        var countryCallPrifix: String? = null
        @SerializedName("country_name")
        @Expose
        var countryName: String? = null
        @SerializedName("country_iso")
        @Expose
        var countryIso: String? = null
        @SerializedName("currency_code")
        @Expose
        var currencyCode: String? = null
        @SerializedName("currency_name")
        @Expose
        var currencyName: String? = null
        @SerializedName("wallet_id")
        @Expose
        var walletId: String? = null
        @SerializedName("wallet_currency_id")
        @Expose
        var walletCurrencyId: String? = null
        @SerializedName("amount")
        @Expose
        var amount: String? = null
        @SerializedName("is_kyc_verified")
        @Expose
        var is_kyc_verified: String? = null
    }
}



