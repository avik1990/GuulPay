package com.guulpay.countryCodes.servicecall

import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object FetchCountryCodeRepositoryProvider {

    fun getCountryCodes(): FetchCountryCodeRepository {
        return FetchCountryCodeRepository(RetrofitClient.getClient()?.create(APIService::class.java)!!)
    }
}