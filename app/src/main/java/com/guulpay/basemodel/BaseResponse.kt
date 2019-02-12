package com.guulpay.basemodel

import com.google.gson.annotations.SerializedName

data class BaseResponse(@SerializedName("response") val response: Response) {

    data class Response(@SerializedName("data") val data: String, @SerializedName("salt") val salt: String)

    override fun toString(): String {
        return super.toString()
    }
}