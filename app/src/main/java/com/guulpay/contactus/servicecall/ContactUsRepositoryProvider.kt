package com.guulpay.contactus.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object ContactUsRepositoryProvider {

    fun getAboutusContent(): ContactusRepository {
        return ContactusRepository(RetrofitClient.getClient()?.create(APIService::class.java)!!)
    }
}