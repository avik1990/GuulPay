package com.guulpay.myProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserEditRequest {

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

        @SerializedName("user_id")
        @Expose
        var userId: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("dob")
        @Expose
        var dob: String? = null
        @SerializedName("gender")
        @Expose
        var gender: String? = null
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

    }


}
