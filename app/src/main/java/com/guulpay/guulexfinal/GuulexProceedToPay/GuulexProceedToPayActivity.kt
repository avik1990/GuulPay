package com.guulpay.guulexfinal.GuulexProceedToPay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.addMoney.addAmount.AddMoneyEnterAmountFragment
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.activity_guulex_proceed_to_pay.*
import kotlinx.android.synthetic.main.activity_guulexfinal.*
import kotlinx.android.synthetic.main.common_toolbar_layout.*

class GuulexProceedToPayActivity : BaseActivity(), View.OnClickListener {


    override fun initResources() {
//        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexProceedToPayFragment.newInstance(),
//                R.id.llproceed_to_pay_activity, false, GuulexProceedToPayFragment.CLASS_NAME)
    }


    override fun initListeners() {
        // if()

        imgvwCommonToolbar.setOnClickListener(this)
        Utils.hideKeyboardOnOutsideTouch(flactivity_proceed_to_pay, this)
        tvCommonToolbar.setCustomFont(this, getString(R.string.quicksandMedium), true)
        tvCommonToolbar.text = getString(R.string.title_guulex_activity_final)
        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexProceedToPayFragment.newInstance(),
                R.id.flactivity_proceed_to_pay, false, GuulexProceedToPayFragment.CLASS_NAME)
    }

    override fun getLayout(): Int {
        return R.layout.activity_guulex_proceed_to_pay
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    override fun onBackPressed() {
        Utils.hideKeyboard(this)
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        if (v == imgvwCommonToolbar) {
            onBackPressed()
        }

    }
    override fun globalLogout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

