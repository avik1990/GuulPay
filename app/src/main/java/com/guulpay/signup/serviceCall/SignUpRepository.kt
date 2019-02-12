package com.guulpay.signup.serviceCall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.others.APIService
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import com.guulpay.signup.model.SignUpRequest
import io.reactivex.Observable

class SignUpRepository(private val apiService: APIService) {

    fun signUpApi(name: String, mobile: String, email: String, passcode: String, c_passcode: String, countrycode: String, device_token: String, user_role: String): Observable<BaseResponse> {
        /* Model class to pass data */
        var signUpRequest = SignUpRequest()
        var signUpRequest_request = SignUpRequest().RequestVariables()
        var signUpRequest_data = SignUpRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.name = name
        signUpRequest_request.phone = mobile
        signUpRequest_request.email = email
        signUpRequest_request.password = passcode
        signUpRequest_request.confirm_password = c_passcode
        signUpRequest_request.country_code = countrycode
        signUpRequest_request.device_type = Constants.Keys.DEVICE_TYPE
        signUpRequest_request.device_token = device_token
        signUpRequest_request.user_role = user_role

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        //ObjectToJSonConvertor.getRequestJson(signUpRequest)
        Log.d("JosnValue", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        // Constants.Values.SIGNUP_DATAS=ObjectToJSonConvertor.getRequestJson(signUpRequest)
        return apiService.callSignUpAPI(signUpRequest)
    }

    fun callCountryCodeAPI(): Observable<BaseResponse> {
        return apiService.callCountryCodeAPI()
    }
}


