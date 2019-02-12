package com.guulpay.signup.serviceCall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object SignUpRepositoryProvider {

    fun getSignUpRepository(): SignUpRepository {
        return SignUpRepository(RetrofitClient.getClient()?.create(APIService::class.java)!!)
    }
}