package com.guulpay.transaction.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransactionHistoryRequest {
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
        var user_id = ""
        @SerializedName("start")
        var start:Int ?= null
        @SerializedName("offset")
        var offset :Int ?= null
        @SerializedName("transaction_type_id")
        var transaction_type_id:String? = ""
        @SerializedName("transaction_from_date")
        var transaction_from_date = ""
        @SerializedName("transaction_to_date")
        var transaction_to_date = ""

    }
}