package com.guulpay.myQrCode

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment

class MyQrCodeFragment: BaseFragment() {

    companion object {
        const val CLASS_NAME = "MyQrCodeFragment"
        fun newInstance(): Fragment {
            return MyQrCodeFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.my_qrcode_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {

    }
}