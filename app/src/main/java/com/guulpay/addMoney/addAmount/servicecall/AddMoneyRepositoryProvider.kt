package com.guulpay.addMoney.addAmount.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object AddMoneyRepositoryProvider {

    fun calladdmoneyAPI(remember_token: String): AddMoneyRepository {
        return AddMoneyRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}