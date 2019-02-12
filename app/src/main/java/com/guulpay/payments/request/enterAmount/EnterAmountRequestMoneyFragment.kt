package com.guulpay.payments.request.enterAmount

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.customUiComponents.EnterAmountEditTextHandler
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import com.guulpay.payments.request.enterPhone.EnterPhoneRequestMoneyFragment
import kotlinx.android.synthetic.main.enter_amount_layout.*
import kotlinx.android.synthetic.main.enter_amount_requestmoney.*

class EnterAmountRequestMoneyFragment : BaseFragment(), View.OnClickListener {

    companion object {
        const val CLASS_NAME = "EnterAmountRequestMoneyFragment"
        fun newInstance(): Fragment {
            return EnterAmountRequestMoneyFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.enter_amount_requestmoney
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        EnterAmountEditTextHandler(etAmount, object : EnterAmountEditTextHandler.EnterAmountEditTextListener {
            override fun isFieldBlank(fieldIsBlank: Boolean, text: String) {
                if (fieldIsBlank){
                    btNextRequestMoney.alpha = 0.5F
                    btNextRequestMoney.isEnabled = false
                }
                else{
                    btNextRequestMoney.alpha = 1.0F
                    btNextRequestMoney.isEnabled = true
                }
            }

        })
    }

    override fun initListeners() {
        btNextRequestMoney.alpha = 0.5F
        btNextRequestMoney.isEnabled = false
        tvAddMsg.setOnClickListener(this)
        btNextRequestMoney.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == btNextRequestMoney){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, EnterPhoneRequestMoneyFragment.newInstance(),
                    R.id.flPaymentContainer, false, EnterPhoneRequestMoneyFragment.CLASS_NAME)
        }
        else if (v == tvAddMsg){
            tvAddMsg.visibility = View.GONE
            etAddMsg.visibility = View.VISIBLE
        }
    }
}