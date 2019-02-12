package com.guulpay.aboutus.servicecall

import android.util.Log
import com.guulpay.aboutus.model.AboutUsRequest
import com.guulpay.basemodel.BaseResponse
import com.guulpay.others.APIService
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class AboutusRepository(private val apiService: APIService) {

    fun callAboutUsApi(): Observable<BaseResponse> {
        var aboutUsRequest = AboutUsRequest()
        var aboutUsRequest_request = AboutUsRequest().RequestVariables()
        var signUpRequest_data = AboutUsRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.slug = "about-us"

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request,randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        aboutUsRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(aboutUsRequest).toString())
        // return apiService.callAboutusAPI(ObjectToJSonConvertor.getRequestJson(aboutUsRequest))
        return apiService.callAboutusPostAPI(aboutUsRequest)
    }
}