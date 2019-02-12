package com.guulpay.myWallet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.addMoney.addAmount.AddMoneyEnterAmountFragment
import com.guulpay.fragments.BaseFragment
import com.guulpay.myWallet.autoLoad.AutoLoadAmountFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.payments.PaymentActivity
import kotlinx.android.synthetic.main.account_fragment.*
import kotlinx.android.synthetic.main.mywallet_fragment.*
import kotlinx.android.synthetic.main.mywallet_menu_layout.*

class MyWalletFragment:BaseFragment(), View.OnClickListener {

    lateinit var recentTransMyWalletAdapter: RecentTransMyWalletAdapter
    var appdata: AppData? = null

    companion object {
        const val CLASS_NAME = "MyWalletFragment"
        fun newInstance(): Fragment {
            return MyWalletFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.mywallet_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appdata = AppData(context!!, Constants.Keys._KeyCryptoPreference)
        tvAvailablebalanceWallet.text=appdata!!.currency_symbol+" "+appdata!!.register_amount

        recentTransMyWalletAdapter = RecentTransMyWalletAdapter()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwRecentTransMyWallet.layoutManager = mLayoutManager
        recyclerVwRecentTransMyWallet.itemAnimator = DefaultItemAnimator()
        recyclerVwRecentTransMyWallet.adapter = recentTransMyWalletAdapter

    }

    override fun initListeners() {
        llSendMoneyMyWallet.setOnClickListener(this)
        llRequestMoneyMyWallet.setOnClickListener(this)
        llAutoLoadMyWallet.setOnClickListener(this)
        llAddMoneyMyWallet.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == llAutoLoadMyWallet){
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, AutoLoadAmountFragment.CLASS_NAME)
            startActivity(intent)
        }
        else if (v == llRequestMoneyMyWallet){
            val intent = Intent(activity, PaymentActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.REQUEST_MONEY_TAB_POSITION)
            startActivity(intent)
        }
        else if (v == llAddMoneyMyWallet){
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, AddMoneyEnterAmountFragment.CLASS_NAME)
            startActivity(intent)
        }
        else if (v == llSendMoneyMyWallet){
            val intent = Intent(activity, PaymentActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.SEND_MONEY_TAB_POSITION)
            startActivity(intent)
        }
    }
}