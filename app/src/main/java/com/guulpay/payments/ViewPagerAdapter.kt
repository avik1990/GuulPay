package com.guulpay.payments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.guulpay.R
import com.guulpay.others.Utils
import com.guulpay.payments.pay.PayViaQrNFCFragment
import com.guulpay.payments.request.enterAmount.EnterAmountRequestMoneyFragment
import com.guulpay.payments.send.ViaPhoneQrFragment
import kotlinx.android.synthetic.main.payment_activity.*

class ViewPagerAdapter(private val fragmentManager: FragmentManager, private val tabCount: Int): FragmentPagerAdapter(fragmentManager) {

    lateinit  var fragmentName: Fragment

    override fun getItem(tabPosition: Int): Fragment {
        when(tabPosition){
            0 -> {
                fragmentName = ViaPhoneQrFragment.newInstance()
            }
            1 -> {
                fragmentName = EnterAmountRequestMoneyFragment.newInstance()
            }
            2 -> {
                fragmentName = PayViaQrNFCFragment.newInstance()
            }
        }
        return fragmentName
    }

    override fun getCount(): Int {
        return tabCount
    }
}