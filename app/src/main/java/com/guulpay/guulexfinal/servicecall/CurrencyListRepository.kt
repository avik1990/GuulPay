package com.guulpay.guulexfinal.servicecall

import android.util.Log
import com.guulpay.basemodel.BaseResponse
import com.guulpay.guulexfinal.guulexforexlist.model.ForExListRequest
import com.guulpay.guulexfinal.models.CalculateAmountRequest
import com.guulpay.guulexfinal.models.CurrencyListRequest
import com.guulpay.guulexfinal.models.ForexCartBuyRequest
import com.guulpay.others.APIService
import com.guulpay.others.Utils
import com.guulpay.others.encryption.ObjectToJSonConvertor
import io.reactivex.Observable


class CurrencyListRepository(private val apiService: APIService){

    fun callCurrencyListApi(base_currency: String): Observable<BaseResponse> {
        //For the Request
        Log.v("base_curr_list_repo",base_currency)
        var currencyList=CurrencyListRequest()
        //for the paramters with the Request
        var currencyListParams=CurrencyListRequest().RequestVariables()
        var randomkey = Utils.generateRandom16bitKey()
        var currencyListRequest=CurrencyListRequest().Request()
        var encryptedstring: String

        currencyListParams.base_currency = base_currency

        Log.v("currencyListRepo",currencyListParams.toString())
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(currencyListParams, randomkey)
        currencyListRequest.data = encryptedstring
        currencyListRequest.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        currencyList.request = currencyListRequest

        Log.v("JsonData",currencyList.toString())

        return apiService.callCurrencyList(currencyList)
    }


    fun callForExAPI(base_currency: String): Observable<BaseResponse> {
        //For the Request
        Log.v("base_curr_forx_repo",base_currency)
        var forExList= ForExListRequest()
        //for the paramters with the Request
        var forExListParams= ForExListRequest().RequestVariables()
        var randomkey = Utils.generateRandom16bitKey()
        var forExListRequest= ForExListRequest().Request()
        var encryptedstring: String

        forExListParams.base_currency = base_currency

        Log.v("forexListrepo",forExListParams.toString())
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(forExListParams, randomkey)
        forExListRequest.data = encryptedstring
        forExListRequest.salt = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")

        //Binding all the data
        forExList.request = forExListRequest

        Log.v("JsonData",forExList.toString())

        return apiService.callForeignExchangeList(forExList)
    }


    fun callCalculateAmountApi(base_currency:String, booking_currency:String, forex_ammount: String, type: String):Observable<BaseResponse> {


        var calAmount=CalculateAmountRequest()
        var calAmountParams=CalculateAmountRequest().RequestVariables()
        var calAmountRequest=CalculateAmountRequest().Request()

        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String

        calAmountParams.base_currency=base_currency
        calAmountParams.booking_currency=booking_currency
        calAmountParams.forex_ammount= forex_ammount
        calAmountParams.type=type
        Log.v("base_currency_repo", calAmountParams.base_currency)
        Log.v("booking_currency_repo",calAmountParams.booking_currency)
        Log.v("forex_ammount_repo",calAmountParams.forex_ammount)
        Log.v("type_repo",calAmountParams.type)

        Log.v("Currencyrepository",calAmountParams.toString())
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(calAmountParams, randomkey)
        calAmountRequest.data=encryptedstring
        calAmountRequest.salt=ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        calAmount.request=calAmountRequest

        Log.d("RequestStringCal", ObjectToJSonConvertor.getRequestJson(calAmount))
        Log.v("calAmountRequest",calAmount.request.toString())

        return apiService.callCalculateAmountList(calAmount)
    }


    fun callForexBuyCartApi(type:Int,user_id: String,base_currency: String,present_country_id:String,forex_data:ArrayList<ForexCartBuyRequest.ForexData>): Observable<BaseResponse> {
        var forexBuyCart=ForexCartBuyRequest()
        var forexBuyCartParams=ForexCartBuyRequest().RequestVariables()
        var forexBuyCartRequest=ForexCartBuyRequest().Request()

        var randomkey = Utils.generateRandom16bitKey()
        var encryptedstring: String

        forexBuyCartParams.type=type
        forexBuyCartParams.user_id=user_id
        forexBuyCartParams.base_currency= base_currency
        forexBuyCartParams.present_country_id=present_country_id
        forexBuyCartParams.forex_data=forex_data

        //Log.v("Currencyrepository",calAmountParams.toString())
        encryptedstring = ObjectToJSonConvertor.getEncryptedString(forexBuyCartParams, randomkey)
        forexBuyCartRequest.data=encryptedstring
        forexBuyCartRequest.salt=ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        forexBuyCart.request=forexBuyCartRequest

       // Log.d("RequestString", ObjectToJSonConvertor.getRequestJson(calAmount))
      //  Log.v("calAmountRequest",calAmount.request.toString())

        return apiService.callForexCartBuy(forexBuyCart)
    }

}