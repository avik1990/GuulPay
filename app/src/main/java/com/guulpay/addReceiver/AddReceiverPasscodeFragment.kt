package com.guulpay.addReceiver

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import com.guulpay.customUiComponents.PasscodeHandler
import kotlinx.android.synthetic.main.addreceiver_passcode_fragment.*
import kotlinx.android.synthetic.main.enter_passcode_layout.*

class AddReceiverPasscodeFragment:BaseFragment(),View.OnClickListener, PasscodeHandler.PasscodeListener {

    companion object {
        const val CLASS_NAME = "AddReceiverPasscodeFragment"
        fun newInstance(): Fragment {
            return AddReceiverPasscodeFragment()
        }
    }

    override fun onClick(v: View?) {
        if (v == btAddReceiver){
            activity?.finish()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.addreceiver_passcode_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        PasscodeHandler(etPasscode1, etPasscode2, etPasscode3, etPasscode4, this)
    }

    override fun initListeners() {
        btAddReceiver.setOnClickListener(this)
    }

    override fun onResult(status: Boolean) {
        if (status){

        }
    }
}