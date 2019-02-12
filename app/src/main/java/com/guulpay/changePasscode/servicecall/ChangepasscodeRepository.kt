package com.guulpay.changePasscode.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.changePasscode.model.ChangePasscodeRequest
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class ChangepasscodeRepository(private val apiService: APIService) {

    fun callChangepasscodeApi(current_password: String, new_password: String, appData: AppData): Observable<BaseResponse> {
        var aboutUsRequest = ChangePasscodeRequest()
        var aboutUsRequest_request = ChangePasscodeRequest().RequestVariables()
        var signUpRequest_data = ChangePasscodeRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        aboutUsRequest_request.userId = appData.user_id
        aboutUsRequest_request.current_password = current_password
        aboutUsRequest_request.new_password = new_password
        aboutUsRequest_request.confirm_password = new_password


        encryptedstring = ObjectToJSonConvertor.getEncryptedString(aboutUsRequest_request,randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        aboutUsRequest.request = signUpRequest_data

        Log.d("OTPDATASENT", ObjectToJSonConvertor.getRequestJson(aboutUsRequest).toString())
        // return apiService.callAboutusAPI(ObjectToJSonConvertor.getRequestJson(aboutUsRequest))
        return apiService.callChangepasswordAPI(aboutUsRequest)
    }
}