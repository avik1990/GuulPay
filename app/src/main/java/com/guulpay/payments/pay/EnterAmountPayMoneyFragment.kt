package com.guulpay.payments.pay

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.customUiComponents.EnterAmountEditTextHandler
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.enter_amount_layout.*
import kotlinx.android.synthetic.main.enter_amount_paymoney.*

class EnterAmountPayMoneyFragment : BaseFragment(), View.OnClickListener {

    companion object {
        const val CLASS_NAME = "EnterAmountPayMoneyFragment"
        fun newInstance(): Fragment {
            return EnterAmountPayMoneyFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.enter_amount_paymoney
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        EnterAmountEditTextHandler(etAmount, object : EnterAmountEditTextHandler.EnterAmountEditTextListener {
            override fun isFieldBlank(fieldIsBlank: Boolean, text: String) {
                if (fieldIsBlank) {
                    btNextPayMoney.alpha = 0.5F
                    btNextPayMoney.isEnabled = false
                } else {
                    btNextPayMoney.alpha = 1.0F
                    btNextPayMoney.isEnabled = true
                }
            }
        })
    }

    override fun initListeners() {
        tvAddMsg.setOnClickListener(this)
    }

    /* View.OnClickListener */
    override fun onClick(v: View?) {
        if (v == tvAddMsg) {
            tvAddMsg.visibility = View.GONE
            etAddMsg.visibility = View.VISIBLE
        }
    }
}