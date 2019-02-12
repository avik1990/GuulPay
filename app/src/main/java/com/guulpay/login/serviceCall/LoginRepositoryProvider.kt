package com.guulpay.login.serviceCall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object LoginRepositoryProvider {

    fun getLoginRepository(): LoginRepository {
        return LoginRepository(RetrofitClient.getClient()!!.create(APIService::class.java))
    }
}