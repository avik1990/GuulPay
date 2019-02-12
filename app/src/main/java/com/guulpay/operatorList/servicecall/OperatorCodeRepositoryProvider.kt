package com.guulpay.operatorList.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object OperatorCodeRepositoryProvider {

    fun getOperatorList(remember_token: String): OperatorCodeRepository {
        return OperatorCodeRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}