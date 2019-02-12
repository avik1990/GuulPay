package com.guulpay.myWallet.autoLoad

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.autoload_fragment.*

class AutoLoadAmountFragment: BaseFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v == btNextAutoLoad){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AutoLoadPasscodeFragment.newInstance(),
                    R.id.flFragmentContainerHome, true, AutoLoadAmountFragment.CLASS_NAME)
        }
    }

    companion object {
        const val CLASS_NAME = "AutoLoadAmountFragment"
        fun newInstance(): Fragment {
            return AutoLoadAmountFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.autoload_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {
        btNextAutoLoad.setOnClickListener(this)
    }
}