package com.guulpay.notification.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NotificationResponse {


    @SerializedName("responseCode")
    @Expose
    var responseCode: Int? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("notification_list")
    @Expose
    var notificationList: List<NotificationList>? = null


    inner class NotificationList {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("user_id")
        @Expose
        var userId: Int? = null
        @SerializedName("subject")
        @Expose
        var subject: String? = null
        @SerializedName("content")
        @Expose
        var content: String? = null
        @SerializedName("type")
        @Expose
        var type: String? = null
        @SerializedName("push_status")
        @Expose
        var pushStatus: Int? = null
        @SerializedName("read_status")
        @Expose
        var readStatus: Int? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

    }

}
