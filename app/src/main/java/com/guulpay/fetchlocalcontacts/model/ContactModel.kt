package com.guulpay.fetchlocalcontacts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ContactModel {

    @SerializedName("responseCode")
    @Expose
    val responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    val responseDetails: String? = null
    @SerializedName("prefixlist")
    @Expose
    val prefixlist: ArrayList<Prefixlist>? = null

    inner class Prefixlist {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("callprefix")
        @Expose
        var callprefix: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null

    }
}