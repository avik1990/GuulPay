package com.guulpay.guulex.servicecall

import com.guulpay.mobilerecharge.servicecall.MobileRechargeRepository
import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object ForexTransactionRepositoryProvider {

    fun getForexTransactionRepository(remember_token: String): ForexTransactionRepository {
        return ForexTransactionRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}