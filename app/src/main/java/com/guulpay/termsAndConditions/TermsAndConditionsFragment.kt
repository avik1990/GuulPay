package com.guulpay.termsAndConditions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment

class TermsAndConditionsFragment:BaseFragment() {

    companion object {
        const val CLASS_NAME = "TermsAndConditionsFragment"
        fun newInstance(): Fragment {
            return TermsAndConditionsFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.terms_conditions_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {

    }
}