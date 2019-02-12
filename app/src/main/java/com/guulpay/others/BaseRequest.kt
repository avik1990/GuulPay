package com.guulpay.others

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BaseRequest : Serializable {

    @SerializedName("response")
    var response = ""

    @SerializedName("data")
    var data = ""

    @SerializedName("saltkey")
    var saltkey = ""
}