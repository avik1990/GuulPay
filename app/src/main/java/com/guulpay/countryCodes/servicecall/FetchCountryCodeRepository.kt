package com.guulpay.countryCodes.servicecall

import com.guulpay.basemodel.BaseResponse
import com.guulpay.others.APIService
import io.reactivex.Observable

class FetchCountryCodeRepository(private val apiService: APIService) {
    fun callCountryCodeAPI(): Observable<BaseResponse> {
        return apiService.callCountryCodeAPI()
    }
}