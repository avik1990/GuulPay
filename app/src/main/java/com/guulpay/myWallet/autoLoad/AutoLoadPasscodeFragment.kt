package com.guulpay.myWallet.autoLoad

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.customUiComponents.PasscodeHandler
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.enter_passcode_layout.*

class AutoLoadPasscodeFragment:BaseFragment(), PasscodeHandler.PasscodeListener {

    companion object {
        const val CLASS_NAME = "AutoLoadPasscodeFragment"
        fun newInstance(): Fragment {
            return AutoLoadPasscodeFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.autoload_passcode_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        PasscodeHandler(etPasscode1, etPasscode2, etPasscode3, etPasscode4, this)
    }

    override fun initListeners() {

    }

    override fun onResult(status: Boolean) {
        Utils.hideKeyboard(activity)
        activity?.finish()
    }
}