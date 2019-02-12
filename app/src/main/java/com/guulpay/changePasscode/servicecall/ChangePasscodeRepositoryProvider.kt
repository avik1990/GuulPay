package com.guulpay.changePasscode.servicecall

import com.guulpay.contactus.servicecall.ContactusRepository
import com.guulpay.myProfile.servicecall.MyProfileRepository
import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object ChangePasscodeRepositoryProvider {

    fun getChangePasscode(remember_token: String): ChangepasscodeRepository {
        return ChangepasscodeRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}