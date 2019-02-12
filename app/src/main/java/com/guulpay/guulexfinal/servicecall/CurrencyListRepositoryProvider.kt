package com.guulpay.guulexfinal.servicecall

import com.guulpay.mobilerecharge.servicecall.MobileRechargeRepository
import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object CurrencyListRepositoryProvider {
    fun getCurrencyRepository(remember_token: String): CurrencyListRepository {
        return CurrencyListRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}


