package com.guulpay.payments.send.paymentStatus

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.payments.PaymentActivity
import kotlinx.android.synthetic.main.sendmoney_paymentstatus_fragment.*

class SendMoneyPaymentStatus: BaseFragment(), View.OnClickListener  {
    override fun onClick(v: View?) {
        if (v == btBackToHome){
            (activity as HomeActivity).finish()
        }
    }

    companion object {
        const val CLASS_NAME = "SendMoneyPaymentStatus"
        fun newInstance(): Fragment {
            return SendMoneyPaymentStatus()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.sendmoney_paymentstatus_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {
        btBackToHome.setOnClickListener(this)
    }
}