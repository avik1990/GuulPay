package com.guulpay.guulexfinal


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.guulpay.guulexfinal.GuulexFinalFragments.GuulexFinalBuyFragment
import com.guulpay.guulexfinal.GuulexFinalFragments.GuulexFinalSellFragment
import com.guulpay.payments.pay.PayViaQrNFCFragment
import com.guulpay.payments.request.enterAmount.EnterAmountRequestMoneyFragment
import com.guulpay.payments.send.ViaPhoneQrFragment

class ViewPagerGuulexFinalAdapter(private val fragmentManager: FragmentManager, private val tabCount: Int):  FragmentPagerAdapter(fragmentManager)  {

    lateinit  var fragmentName: Fragment

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
               fragmentName=GuulexFinalBuyFragment.newInstance()
            }
            1 -> {
                fragmentName = GuulexFinalSellFragment.newInstance()
            }

        }
        return fragmentName
    }

    override fun getCount(): Int {
        return tabCount
    }
}