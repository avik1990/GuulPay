package com.guulpay.addMoney.addAmount.servicecall

import android.util.Log
import com.guulpay.addMoney.addAmount.model.AddMoneyWalletRequest
import com.guulpay.basemodel.BaseResponse
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class AddMoneyRepository(private val apiService: APIService) {

    fun callWalletRechargeApi(amt: String, appData: AppData): Observable<BaseResponse> {

        var signUpRequest = AddMoneyWalletRequest()
        var signUpRequest_request = AddMoneyWalletRequest().RequestVariables()
        var signUpRequest_data = AddMoneyWalletRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()

        var encryptedstring: String
        signUpRequest_request.userId = appData.user_id
        signUpRequest_request.walletId = appData.register_wallet_id
        signUpRequest_request.amount = amt.toString()
        signUpRequest_request.currencyCode = appData.register_currency_code
        signUpRequest_request.name = appData.register_name
        signUpRequest_request.email = appData.register_email
        signUpRequest_request.phone = appData.register_phone
        signUpRequest_request.callPrefix = appData.country_call_prifix
        signUpRequest_request.is_kyc_verified = appData.is_kyc_verified


        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        //ObjectToJSonConvertor.getRequestJson(signUpRequest)
        Log.d("JosnValue_addmoney", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        // Constants.Values.SIGNUP_DATAS=ObjectToJSonConvertor.getRequestJson(signUpRequest)
        return apiService.calladdmoenttowalletAPI(signUpRequest)

    }
}