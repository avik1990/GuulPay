package com.guulpay.myProfile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserImageUploadResponse {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("original_image")
    @Expose
    var originalImage: String? = null
    @SerializedName("small_image")
    @Expose
    var smallImage: String? = null
}
