package com.guulpay.fcm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class NotificationModel {

    @SerializedName("msg")
    @Expose
    var msg: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("subtitle")
    @Expose
    var subtitle: String? = null
}