package com.guulpay.operatorList.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.operatorList.models.OperatorListRequest
import com.guulpay.others.APIService
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class OperatorCodeRepository(private val apiService: APIService) {

    fun callOperatorAPI(country_id: String): Observable<BaseResponse> {
        var signUpRequest = OperatorListRequest()
        var signUpRequest_request = OperatorListRequest().RequestVariables()
        var signUpRequest_data = OperatorListRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.country_id = country_id

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request,randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        //ObjectToJSonConvertor.getRequestJson(signUpRequest)
        Log.d("JosnValue", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        // Constants.Values.SIGNUP_DATAS=ObjectToJSonConvertor.getRequestJson(signUpRequest)
        return apiService.callOperatorListAPI(signUpRequest)
    }
}