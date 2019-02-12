package com.guulpay.myProfile.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object MyProfileRepositoryProvider {

    fun getUserDetsils(remember_token: String): MyProfileRepository {
        return MyProfileRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}