package com.guulpay.operatorList.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OperatorModel {

    @SerializedName("responseCode")
    @Expose
    var responseCode: String? = null
    @SerializedName("responseDetails")
    @Expose
    var responseDetails: String? = null
    @SerializedName("operatorlist")
    @Expose
    var operatorlist: ArrayList<Operatorlist>? = null



    inner class Operatorlist {

        @SerializedName("operator_id")
        @Expose
        public var operatorId: String? = null
        @SerializedName("operator")
        @Expose
        public var operator: String? = null
        @SerializedName("country_id")
        @Expose
        public var countryId: String? = null
        @SerializedName("country")
        @Expose
        public var country: String? = null
        @SerializedName("operator_logo")
        @Expose
        public var operator_logo: String? = null
    }
}