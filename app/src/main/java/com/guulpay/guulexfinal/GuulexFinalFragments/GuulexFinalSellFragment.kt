package com.guulpay.guulexfinal.GuulexFinalFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment

class GuulexFinalSellFragment :BaseFragment() {

    companion object {
        const val CLASS_NAME = "GuulexFinalSellFragment"
        fun newInstance(): Fragment {
            return GuulexFinalSellFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.guulex_final_sell_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {

    }

}