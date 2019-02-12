package com.guulpay.payments.send.enterAmount

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.customUiComponents.EnterAmountEditTextHandler
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import com.guulpay.payments.send.enterPhone.EnterPhoneSendMoneyFragment
import kotlinx.android.synthetic.main.enter_amount_layout.*
import kotlinx.android.synthetic.main.enter_amount_sendmoney.*

class EnterAmountSendMoneyFragment:BaseFragment(), View.OnClickListener {

    companion object {
        const val CLASS_NAME = "EnterAmountSendMoneyFragment"
        fun newInstance(): Fragment {
            return EnterAmountSendMoneyFragment()
        }
    }

    override fun getLayoutView(): Int {
       return R.layout.enter_amount_sendmoney
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        EnterAmountEditTextHandler(etAmount, object : EnterAmountEditTextHandler.EnterAmountEditTextListener {
            override fun isFieldBlank(fieldIsBlank: Boolean, text: String) {
                if (fieldIsBlank){
                    btNextSendMoney.alpha = 0.5F
                    btNextSendMoney.isEnabled = false
                }
                else{
                    btNextSendMoney.alpha = 1.0F
                    btNextSendMoney.isEnabled = true
                }
            }

        })
    }

    override fun initListeners() {
        btNextSendMoney.alpha = 0.5F
        btNextSendMoney.isEnabled = false
        tvAddMsg.setOnClickListener(this)
        btNextSendMoney.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == btNextSendMoney){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, EnterPhoneSendMoneyFragment.newInstance(),
                    R.id.flPaymentContainer, false, EnterPhoneSendMoneyFragment.CLASS_NAME)
        }
        else if (v == tvAddMsg){
            tvAddMsg.visibility = View.GONE
            etAddMsg.visibility = View.VISIBLE
        }
    }
}