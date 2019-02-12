package com.guulpay.transaction.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.guulpay.guulexfinal.guulexforexlist.model.ForExListResponse

class TransactionHistoryResponse {
    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("transaction_list")
    @Expose
    var transaction_list: TransactionHistoryResponse.TransactionListArray? = null


    inner class TransactionListArray {
        @SerializedName("")
        @Expose
        var transaction_history_date: ArrayList<TransactionHistoryResponse.TransactionData>? = null


    }

    inner class TransactionData {
        @SerializedName("id")
        @Expose
        private val id: String? = null
        @SerializedName("relation_parent_id")
        @Expose
        val relation_parent_id: String? = null
        @SerializedName("status")
        @Expose
        val status: String? = null
        @SerializedName("debit")
        @Expose
        val debit: String? = null
        @SerializedName("credit")
        @Expose
        private val credit: String? = null
        @SerializedName("totalAmount")
        @Expose
        private val totalAmount: String? = null


        @SerializedName("user_currency_code")
        @Expose
        private val user_currency_code: String? = null

        @SerializedName("credit_currency_code")
        @Expose
        private val credit_currency_code: String? = null

        @SerializedName("forex_total_amount")
        @Expose
        private val forex_total_amount: String? = null

        @SerializedName("forex_paid_amount")
        @Expose
        private val forex_paid_amount: String? = null


        @SerializedName("forex_remaining_amount")
        @Expose
        private val forex_remaining_amount: String? = null

        @SerializedName("mobile_recharge_call_prifix")
        @Expose
        private val mobile_recharge_call_prifix: String? = null

        @SerializedName("mobile_recharge_phone_number")
        @Expose
        private val mobile_recharge_phone_number: String? = null


        @SerializedName("third_party_trasaction_code")
        @Expose
        private val third_party_trasaction_code: String? = null

        @SerializedName("additional_info")
        @Expose
        private val additional_info: String? = null

        @SerializedName("full_status")
        @Expose
        private val full_status: String? = null

        @SerializedName("short_message")
        @Expose
        private val short_message: String? = null

        @SerializedName("created_at")
        @Expose
        private val created_at: String? = null
        @SerializedName("date")
        @Expose
        private val date: String? = null







    }
}