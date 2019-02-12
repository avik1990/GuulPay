package com.guulpay.others

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var retrofit1: Retrofit? = null


    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(Constants.Services.BASE_URL)
                    .client(Constants.setTimeOut())
                    .build()
        }
        return retrofit
    }

    fun getClientAfterLogin(remember_token: String): Retrofit? {
        if (retrofit1 == null) {
            retrofit1 = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(Constants.Services.BASE_URL)
                    .client(Constants.setTimeOutAfterLogin(remember_token))
                    .build()
        }
        return retrofit1
    }


    fun clearRetrofitInstance() {
        retrofit = null
        retrofit1 = null
    }

}