package com.guulpay.mobilerecharge.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.mobilerecharge.model.MobileRechargeRequest
import com.guulpay.mobilerecharge.model.MsisdninfoRequest
import com.guulpay.mobilerecharge.model.TransactionRequest
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class MobileRechargeRepository(private val apiService: APIService) {
    //user_id, operator_id, mobile_no, country_id, amount, isWalletselected
    fun callRecharge(user_id: String, recharege_currency_code: String, mobile: String, callprefix: String, amount: String, isWalletselected: String, appData: AppData): Observable<BaseResponse> {
        /* Model class to pass data */
        var signUpRequest = MobileRechargeRequest()
        var signUpRequest_request = MobileRechargeRequest().RequestVariables()
        var signUpRequest_data = MobileRechargeRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.user_id = user_id
        // signUpRequest_request.operator_id = operator_id
        signUpRequest_request.phone = mobile
        //signUpRequest_request.country_id = country_id
        signUpRequest_request.amount = amount
        signUpRequest_request.isWalletselected = isWalletselected
        signUpRequest_request.wallet_id = appData.register_wallet_id
        signUpRequest_request.call_prefix = callprefix
        signUpRequest_request.currency_code = recharege_currency_code
        signUpRequest_request.base_currency_code = appData.register_currency_code


        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        //ObjectToJSonConvertor.getRequestJson(signUpRequest)
        Log.d("JosnValue", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        // Constants.Values.SIGNUP_DATAS=ObjectToJSonConvertor.getRequestJson(signUpRequest)
        return apiService.callmobileRechargeApi(signUpRequest)
    }

    fun callCountryCodeAPI(): Observable<BaseResponse> {
        return apiService.callCountryCodeAPI()
    }

    fun callMsisdninfo(call_prefix: String, phone: String, appData: AppData): Observable<BaseResponse> {
        var signUpRequest = MsisdninfoRequest()
        var signUpRequest_request = MsisdninfoRequest().RequestVariables()
        var signUpRequest_data = MsisdninfoRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.call_prefix = call_prefix
        signUpRequest_request.phone = phone

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        Log.d("JosnValue", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        return apiService.callMsisdninfoApi(signUpRequest)
    }


    fun callRechargehist(start: Int, offset: Int, appData: AppData): Observable<BaseResponse> {
        var signUpRequest = TransactionRequest()
        var signUpRequest_request = TransactionRequest().RequestVariables()
        var signUpRequest_data = TransactionRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.user_id = appData.user_id
        signUpRequest_request.start = start.toString()
        signUpRequest_request.offset = offset.toString()

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        Log.d("JosnValue", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        return apiService.callRechargehistApi(signUpRequest)
    }

}


