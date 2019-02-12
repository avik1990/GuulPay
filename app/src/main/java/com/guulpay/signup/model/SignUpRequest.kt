package com.guulpay.signup.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SignUpRequest {

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
        @SerializedName("name")
        var name = ""

        @SerializedName("phone")
        var phone = ""

        @SerializedName("email")
        var email = ""

        @SerializedName("password")
        var password = ""

        @SerializedName("confirm_password")
        var confirm_password = ""

        @SerializedName("country_code")
        var country_code = ""

        @SerializedName("user_role")
        var user_role = ""

        @SerializedName("device_type")
        var device_type = ""

        @SerializedName("device_token")
        var device_token = ""

        override fun toString(): String {
            return "variables(name='$name', phone='$phone', email='$email', password='$password', confirm_password='$confirm_password', country_code='$country_code', user_role='$user_role', device_type='$device_type', device_token='$device_token')"
        }
    }
}
