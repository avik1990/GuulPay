package com.guulpay.addMoney.paymentStatus

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.addmoney_paymentstatus_fragment.*

class AddMoneyPaymentStatusFragment: BaseFragment(), View.OnClickListener  {
    override fun onClick(v: View?) {
        if (v == btBackToHome){
            (activity as HomeActivity).finish()
        }
    }

    companion object {
        const val CLASS_NAME = "AddMoneyPaymentStatusFragment"
        fun newInstance(): Fragment {
            return AddMoneyPaymentStatusFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.addmoney_paymentstatus_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {
        btBackToHome.setOnClickListener(this)
    }
}