package com.guulpay.myProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class UserDetailsResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("user")
    @Expose
    var user: User? = null


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
        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = null
        @SerializedName("device_type")
        @Expose
        var deviceType: String? = null
        @SerializedName("address_line_1")
        @Expose
        var addressLine1: String? = null
        @SerializedName("address_line_2")
        @Expose
        var addressLine2: String? = null
        @SerializedName("city")
        @Expose
        var city: String? = null
        @SerializedName("state")
        @Expose
        var state: String? = null
        @SerializedName("zipcode")
        @Expose
        var zipcode: String? = null
        @SerializedName("currencyId")
        @Expose
        var currencyId: String? = null
        @SerializedName("newletter_subscribe")
        @Expose
        var newletterSubscribe: String? = null
        @SerializedName("is_kyc_verified")
        @Expose
        var is_kyc_verified: String? = null
    }
}
