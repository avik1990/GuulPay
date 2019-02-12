package com.guulpay.enterOtp.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.enterOtp.model.EnterOTPRequest
import com.guulpay.enterOtp.model.ForceLoginRequest
import com.guulpay.enterOtp.model.OtpafterrigistrationRequest
import com.guulpay.enterOtp.model.VerifyOTPRequest
import com.guulpay.forgotPasscode.model.ResendOTPRequest
import com.guulpay.others.*
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class EnterOTPRepository(private val apiService: APIService) {
    fun getotpafterrigistrationRepositoryApi(appData: AppData): Observable<BaseResponse> {
        /* Model class to pass data */
        var signUpRequest = OtpafterrigistrationRequest()
        var signUpRequest_request = OtpafterrigistrationRequest().EnterOTPRequest()
        var signUpRequest_data = OtpafterrigistrationRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.emailorphone = appData.register_phone
        signUpRequest_request.country_code = appData.register_country_code
        signUpRequest_request.device_type = Constants.Keys.DEVICE_TYPE

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(signUpRequest).toString())

        return apiService.callotpafterrigistration(signUpRequest)
    }


    fun loginafterrigistration(otp: String, appData: AppData): Observable<BaseResponse> {

        var signUpRequest = EnterOTPRequest()
        var signUpRequest_request = EnterOTPRequest().RequestVariables()
        var signUpRequest_data = EnterOTPRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.emailorphone = appData.register_phone
        signUpRequest_request.password = appData.register_password
        signUpRequest_request.device_type = Constants.Keys.DEVICE_TYPE
        signUpRequest_request.device_token = appData.save_devicetoken
        signUpRequest_request.country_code = appData.register_country_code
        signUpRequest_request.otp = otp

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(signUpRequest).toString())
        return apiService.callloginafterrigistration(signUpRequest)
    }


    fun forceafterrigistration(otp: String, appData: AppData): Observable<BaseResponse> {

        var signUpRequest = ForceLoginRequest()
        var signUpRequest_request = ForceLoginRequest().RequestVariables()
        var signUpRequest_data = ForceLoginRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.phone = appData.register_phone
        signUpRequest_request.device_type = Constants.Keys.DEVICE_TYPE
        signUpRequest_request.device_token = appData.save_devicetoken
        signUpRequest_request.otp = otp

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
        Log.d("FORCELOGIDATASENT", ObjectToJSonConvertor.getRequestJson(signUpRequest).toString())
        return apiService.callforceloginApi(signUpRequest)
    }


    fun verifyOTP(otp: String, appData: AppData): Observable<BaseResponse> {
        var signUpRequest = VerifyOTPRequest()
        var signUpRequest_request = VerifyOTPRequest().RequestVariables()
        var signUpRequest_data = VerifyOTPRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.phone = appData.register_phone
        signUpRequest_request.country_code = appData.register_country_code
        signUpRequest_request.otp = otp

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
        Log.d("VERIFYOTP", ObjectToJSonConvertor.getRequestJson(signUpRequest).toString())
        return apiService.callVerifyOTPAPI(signUpRequest)
    }

    fun resendOTP(phoneno: String): Observable<BaseResponse> {
        var signUpRequest = ResendOTPRequest()
        var signUpRequest_request = ResendOTPRequest().RequestVariables()
        var signUpRequest_data = ResendOTPRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.phone = phoneno

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
        Log.d("VERIFYOTP", ObjectToJSonConvertor.getRequestJson(signUpRequest).toString())
        return apiService.callResendOTPAPI(signUpRequest)
    }


    fun callotpafterloginApi(appData: AppData): Observable<BaseResponse> {
        var signUpRequest = VerifyOTPRequest()
        var signUpRequest_request = VerifyOTPRequest().RequestVariablesOrpAfterLogin()
        var signUpRequest_data = VerifyOTPRequest().Request()
        var randomkey =Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.emailorphone = appData.register_phone

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request,randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data
        Log.d("VERIFYOTP", ObjectToJSonConvertor.getRequestJson(signUpRequest).toString())
        return apiService.callotpafterloginApi(signUpRequest)
    }


}