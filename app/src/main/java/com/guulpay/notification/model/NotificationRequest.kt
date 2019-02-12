package com.guulpay.notification.modelRechargelist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NotificationRequest {

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
        @SerializedName("start")
        var start = ""
        @SerializedName("offset")
        var offset = ""
    }
}
