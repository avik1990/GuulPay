package com.guulpay.transaction.servicecall


import com.guulpay.others.APIService
import com.guulpay.others.RetrofitClient

object TransactionHistoryRepositoryProvider {
    fun getTranactionHistory(remember_token: String): TransactionHistoryRepository {
        return TransactionHistoryRepository(RetrofitClient.getClientAfterLogin(remember_token)?.create(APIService::class.java)!!)
    }
}