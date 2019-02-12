package com.guulpay.aboutus.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AboutUsRequest {

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
        @SerializedName("slug")
        @Expose
        var slug: String? = null
    }
}
