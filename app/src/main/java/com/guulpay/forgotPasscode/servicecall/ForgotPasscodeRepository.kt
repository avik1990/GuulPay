package com.guulpay.forgotPasscode.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.forgotPasscode.model.CreatePasswordRequest
import com.guulpay.forgotPasscode.model.ForgotPasswordRequest
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class ForgotPasscodeRepository(private val apiService: APIService) {

    fun forgotpasscodeApi(phone: String, country_code: String): Observable<BaseResponse> {
        /* Model class to pass data */
        var forgotPasswordRequest = ForgotPasswordRequest()
        var forgotPasswordRequest_var = ForgotPasswordRequest().RequestVariables()
        var signUpRequest_data = ForgotPasswordRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        forgotPasswordRequest_var.phone = phone
        forgotPasswordRequest_var.country_code = country_code

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(forgotPasswordRequest_var, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        forgotPasswordRequest.request = signUpRequest_data
        Log.d("Forgotpassword_response", "" + ObjectToJSonConvertor.getRequestJson(forgotPasswordRequest))
        return apiService.callForgotpasswordAPI(forgotPasswordRequest)
    }

    fun resetpasscodeApi(passcode: String, appdata: AppData): Observable<BaseResponse> {
        /* Model class to pass data */
        var forgotPasswordRequest = CreatePasswordRequest()
        var forgotPasswordRequest_var = CreatePasswordRequest().RequestVariables()
        var signUpRequest_data = CreatePasswordRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        forgotPasswordRequest_var.phone = appdata.register_phone
        forgotPasswordRequest_var.password = passcode
        forgotPasswordRequest_var.confirmPassword = passcode

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(forgotPasswordRequest_var, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        forgotPasswordRequest.request = signUpRequest_data

        return apiService.callcreatenewpasswordAPI(forgotPasswordRequest)
    }
}