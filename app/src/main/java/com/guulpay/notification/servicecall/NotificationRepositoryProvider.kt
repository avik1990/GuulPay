package com.guulpay.notification.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object NotificationRepositoryProvider {

    fun callNotificationApi(remember_token: String): NotificationRepository {
        return NotificationRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}