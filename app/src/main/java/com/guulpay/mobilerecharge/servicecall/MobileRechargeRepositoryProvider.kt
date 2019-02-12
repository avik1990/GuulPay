package com.guulpay.mobilerecharge.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object MobileRechargeRepositoryProvider {

    fun getRechargeRepository(remember_token: String): MobileRechargeRepository {
        return MobileRechargeRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}