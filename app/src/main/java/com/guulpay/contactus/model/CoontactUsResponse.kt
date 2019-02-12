package com.guulpay.contactus.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CoontactUsResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("slug")
    @Expose
    var slug: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("extra")
    @Expose
    var extra: String? = null


}
