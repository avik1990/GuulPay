package com.guulpay.forgotPasscode.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object ForgotPasscodeRepositoryProvider {

    fun getForgotPasscodeRepository(): ForgotPasscodeRepository {
        return ForgotPasscodeRepository(RetrofitClient.getClient()?.create(APIService::class.java)!!)
    }
}