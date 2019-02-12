package com.guulpay.notification.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.notification.modelRechargelist.NotificationRequest
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class NotificationRepository(private val apiService: APIService) {

    fun callNotificationData(start: Int, offset: Int, appData: AppData): Observable<BaseResponse> {

        var signUpRequest = NotificationRequest()
        var signUpRequest_request = NotificationRequest().RequestVariables()
        var signUpRequest_data = NotificationRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()

        var encryptedstring: String
        signUpRequest_request.userId = appData.user_id
        signUpRequest_request.start = start.toString()
        signUpRequest_request.offset = offset.toString()

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
        Log.v("Notification_request",ObjectToJSonConvertor.getRequestJson(signUpRequest))

        return apiService.callnotificationRequest(signUpRequest)
    }
}