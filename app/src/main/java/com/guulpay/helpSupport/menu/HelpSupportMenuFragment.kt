package com.guulpay.helpSupport.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.calldetails_helpsupport_layout.*
import kotlinx.android.synthetic.main.help_support_menu_fragment.*

class HelpSupportMenuFragment: BaseFragment(), View.OnClickListener {

    lateinit var helpSupportMenuAdapter: HelpSupportMenuAdapter
    companion object {
        const val CLASS_NAME = "HelpSupportMenuFragment"
        fun newInstance(): Fragment {
            return HelpSupportMenuFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.help_support_menu_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        /* Menu Grid items */
        helpSupportMenuAdapter = HelpSupportMenuAdapter(activity as DashboardHomeActivity?)
        val mGridManager = GridLayoutManager(activity,2)
        recyclerVwMenuGridHelpSupport.layoutManager = mGridManager
        recyclerVwMenuGridHelpSupport.itemAnimator = DefaultItemAnimator()

        helpSupportMenuAdapter.addItems(HelpSupportMenuModel(getString(R.string.rechargeBillPayment), activity?.resources?.getDrawable(R.drawable.ic_recharge_icon_2),
                "Mobile Recharge, Guulex, Cards , Ayuuto"))
        helpSupportMenuAdapter.addItems(HelpSupportMenuModel(getString(R.string.guulpayWallet), activity?.resources?.getDrawable(R.drawable.ic_wallte_icon_2),
                "Wallet transaction & balance"))
        helpSupportMenuAdapter.addItems(HelpSupportMenuModel(getString(R.string.linkToBankTrans), activity?.resources?.getDrawable(R.drawable.ic_bank_icon_2),
                "Bank link, Bank Transfer, Add Receiver"))
        helpSupportMenuAdapter.addItems(HelpSupportMenuModel(getString(R.string.othersSupport), activity?.resources?.getDrawable(R.drawable.ic_other_support_icon_2),
                "Any other issue related support"))

        recyclerVwMenuGridHelpSupport.adapter = helpSupportMenuAdapter
    }



    override fun initListeners() {
        imgvwCallHelpSupport.setOnClickListener(this)
    }

    /* View.OnClickListener  */
    override fun onClick(v: View?) {
        if (v == imgvwCallHelpSupport){
            Utils.setPhoneCall(tvCallNumberHelpSupport.text.toString(), context)
        }
    }
}