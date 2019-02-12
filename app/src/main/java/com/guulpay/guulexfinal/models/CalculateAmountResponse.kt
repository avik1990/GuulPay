package com.guulpay.guulexfinal.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CalculateAmountResponse{

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("original_rate")
    @Expose
    var originalRate: String? = null
    @SerializedName("rate_with_admin_percent")
    @Expose
    var rateWithAdminPercent: Double? = null
    @SerializedName("final_amount_normal_rate")
    @Expose
    var finalAmountNormalRate: Double? = null
    @SerializedName("final_amount_commission_rate")
    @Expose
    var finalAmountCommissionRate: Double? = null
}