package com.guulpay.login.serviceCall

import com.guulpay.basemodel.BaseResponse
import com.guulpay.login.models.LoginRequestModel
import com.guulpay.others.*
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class LoginRepository(private val apiService: APIService) {

    fun callLoginApi(passcode: String, phoneno: String,appData: AppData): Observable<BaseResponse> {
        /* Model class to pass data */
        var signUpRequest = LoginRequestModel()
        var signUpRequest_request = LoginRequestModel().RequestVariables()
        var signUpRequest_data = LoginRequestModel().Request()
        var randomkey =Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.emailorphone = phoneno
        signUpRequest_request.password = passcode
        signUpRequest_request.deviceType = Constants.Keys.DEVICE_TYPE
        signUpRequest_request.deviceToken = appData.save_devicetoken



        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request,randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
       // Log.d("RequestString", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        return apiService.callLoginAPI(signUpRequest)
    }
}