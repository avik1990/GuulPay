package com.guulpay.guulexfinal.GuulexProceedToPay

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import kotlinx.android.synthetic.main.guulex_final_proceedtopay_fragment.*

class GuulexProceedToPayFragment : BaseFragment(){

    var appData: AppData? = null
    companion object {
        const val CLASS_NAME = "GuulexProceedToPayFragment"
        fun newInstance(): Fragment {
            return GuulexProceedToPayFragment()
        }
    }


    override fun getLayoutView(): Int {
        return R.layout.guulex_final_proceedtopay_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(this!!.context!!, Constants.Keys._KeyCryptoPreference)
        tvFirstNameDetails.text=appData!!.register_name
        tvPhoneDetails.text="+"+appData!!.country_call_prifix+" "+appData!!.register_phone
        tvEmailDetails.text=appData!!.register_email

    }

    override fun initListeners() {
        //TODO: implement later
    }


}