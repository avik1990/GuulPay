package com.guulpay.guulex.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.guulex.model.BaseCurrencyRequest
import com.guulpay.others.APIService
import com.guulpay.others.AppData
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable

class ForexTransactionRepository(private val apiService: APIService) {
    //user_id, operator_id, mobile_no, country_id, amount, isWalletselected

    /*  fun calltransactionAPI(): Observable<BaseResponse> {
          return apiService.callTransactionListAPI()
      }*/


    fun calltransactionAPI(appData: AppData): Observable<BaseResponse> {
        /* Model class to pass data */
        var signUpRequest = BaseCurrencyRequest()
        var signUpRequest_request = BaseCurrencyRequest().RequestVariables()
        var signUpRequest_data = BaseCurrencyRequest().Request()
        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String
        signUpRequest_request.base_currency = appData.register_currency_code

        encryptedstring = ObjectToJSonConvertor.getEncryptedString(signUpRequest_request, randomkey)
        signUpRequest_data.data = encryptedstring
        signUpRequest_data.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        signUpRequest.request = signUpRequest_data

        //ObjectToJSonConvertor.getRequestJson(signUpRequest)
        Log.d("JosnValue", ObjectToJSonConvertor.getRequestJson(signUpRequest))
        // Constants.Values.SIGNUP_DATAS=ObjectToJSonConvertor.getRequestJson(signUpRequest)
        return apiService.callTransactionListAPI(signUpRequest)
    }


}


