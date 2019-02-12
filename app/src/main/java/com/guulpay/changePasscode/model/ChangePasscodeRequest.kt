package com.guulpay.changePasscode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ChangePasscodeRequest {

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
        @SerializedName("current_password")
        @Expose
        var current_password: String? = null
        @SerializedName("new_password")
        @Expose
        var new_password: String? = null
        @SerializedName("confirm_password")
        @Expose
        var confirm_password: String? = null
    }


}
