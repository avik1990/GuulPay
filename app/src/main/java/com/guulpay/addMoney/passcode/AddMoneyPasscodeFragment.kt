package com.guulpay.addMoney.passcode

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addMoney.paymentStatus.AddMoneyPaymentStatusFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.customUiComponents.PasscodeHandler
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.enter_passcode_layout.*

class AddMoneyPasscodeFragment: BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    companion object {
        const val CLASS_NAME = "AddMoneyPasscodeFragment"
        fun newInstance(): Fragment {
            return AddMoneyPasscodeFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.addmoney_passcode_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        PasscodeHandler(etPasscode1, etPasscode2, etPasscode3, etPasscode4, object : PasscodeHandler.PasscodeListener {
            override fun onResult(status: Boolean) {
                if (status) {
                    (activity as HomeActivity).setUpScreenUiForFragment(AddMoneyPaymentStatusFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddMoneyPaymentStatusFragment.newInstance(),
                            R.id.flFragmentContainerHome, true, AddMoneyPaymentStatusFragment.CLASS_NAME)
                }
            }

        })
    }

    override fun initListeners() {
    }
}