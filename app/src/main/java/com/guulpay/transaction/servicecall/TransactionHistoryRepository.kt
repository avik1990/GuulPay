package com.guulpay.transaction.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.guulexfinal.models.ForexCartBuyRequest
import com.guulpay.others.APIService
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import com.guulpay.transaction.model.TransactionHistoryRequest
import com.guulpay.transaction.model.TransactionHistoryResponse
import io.reactivex.Observable

class TransactionHistoryRepository (private val apiService: APIService){

    fun callTransactionHistoryAPI(user_id: String,start:Int,offset:Int,transaction_type_id:String,transaction_from_date:String, transaction_to_date:String): Observable<BaseResponse> {
        var transactionHistory= TransactionHistoryRequest()
        var transactionHistoryParams= TransactionHistoryRequest().RequestVariables()
        var transactionHistoryRequest= TransactionHistoryRequest().Request()

        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String

        transactionHistoryParams.user_id=user_id
        transactionHistoryParams.start=start
        transactionHistoryParams.offset= offset
        transactionHistoryParams.transaction_type_id=transaction_type_id
        transactionHistoryParams.transaction_from_date=transaction_from_date
        transactionHistoryParams.transaction_to_date=transaction_to_date

        Log.v("Transaction_data",transaction_type_id)
        //Log.v("Currencyrepository",calAmountParams.toString())
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(transactionHistoryParams, randomkey)
        transactionHistoryRequest.data=encryptedstring
        transactionHistoryRequest.salt= ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        transactionHistory.request=transactionHistoryRequest

        // Log.d("RequestString", ObjectToJSonConvertor.getRequestJson(calAmount))
        //  Log.v("calAmountRequest",calAmount.request.toString())

        return apiService.callTransactionHistory(transactionHistory)
    }

}