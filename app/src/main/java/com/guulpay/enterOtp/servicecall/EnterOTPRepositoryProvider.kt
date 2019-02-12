package com.guulpay.enterOtp.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object EnterOTPRepositoryProvider {

    fun getotpafterrigistrationRepository(): EnterOTPRepository {
        return EnterOTPRepository(RetrofitClient.getClient()?.create(APIService::class.java)!!)
    }
}