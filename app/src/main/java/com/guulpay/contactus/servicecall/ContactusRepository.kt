package com.guulpay.contactus.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.contactus.model.ContactUsRequest
import com.guulpay.others.APIService
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class ContactusRepository(private val apiService: APIService) {

    fun callContactUsApi(name: String, email: String, message: String): Observable<BaseResponse> {
        var aboutUsRequest = ContactUsRequest()
        var aboutUsRequest_request = ContactUsRequest().RequestVariables()
        var signUpRequest_data = ContactUsRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.name = name
        aboutUsRequest_request.email = email
        aboutUsRequest_request.message = message


        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        aboutUsRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(aboutUsRequest).toString())
        // return apiService.callAboutusAPI(ObjectToJSonConvertor.getRequestJson(aboutUsRequest))
        return apiService.callContactusPostAPI(aboutUsRequest)
    }
}