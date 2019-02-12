package com.guulpay.aboutus.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object AboutUsRepositoryProvider {

    fun getAboutusContent(): AboutusRepository {
        return AboutusRepository(RetrofitClient.getClient()?.create(APIService::class.java)!!)
    }
}