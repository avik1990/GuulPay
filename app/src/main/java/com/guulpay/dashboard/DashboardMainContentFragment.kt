package com.guulpay.dashboard

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.addBankAccount.AddBankAccountFragment
import com.guulpay.addMoney.addAmount.AddMoneyEnterAmountFragment
import com.guulpay.basemodel.CurrenciesModel
import com.guulpay.dashboard.adapters.RecentTransactionsAdapter
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulex.GuulexFragment
import com.guulpay.guulexfinal.GuulexfinalActivity
import com.guulpay.mobilerecharge.MobileRechargeFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.payments.PaymentActivity
import com.guulpay.viewAllRecentTransactions.ViewAllRecentTransFragment
import kotlinx.android.synthetic.main.dashboard_maincontent_fragment.*
import kotlinx.android.synthetic.main.recent_transactions_layout.*
import kotlinx.android.synthetic.main.recharge_billpayment_layout.*
import kotlinx.android.synthetic.main.topmenu_layout.*
import java.io.IOException

class DashboardMainContentFragment : BaseFragment(), View.OnClickListener {

    lateinit var recentTransactionsAdapter: RecentTransactionsAdapter
    var currenciesModellist: ArrayList<CurrenciesModel> = ArrayList()

    companion object {
        const val CLASS_NAME = "DashboardMainContentFragment"
        fun newInstance(): Fragment {
            return DashboardMainContentFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    var appData: AppData? = null

    override fun getLayoutView(): Int {
        return R.layout.dashboard_maincontent_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context as DashboardHomeActivity, Constants.Keys._KeyCryptoPreference)
        /* Recent Transactions list */
        recentTransactionsAdapter = RecentTransactionsAdapter()
        recyclerVwRecentTrans.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerVwRecentTrans.itemAnimator = DefaultItemAnimator()
        recyclerVwRecentTrans.adapter = recentTransactionsAdapter
        /*tvWalletAmt.text = appData!!.register_currency_code + " " + appData!!.register_amount*/
        /* To make the ScrollView scrolled to top position */
        scrollVwHomeParent.smoothScrollTo(0, 0)
    }

    override fun initListeners() {
        horizontalRechargeView.fullScroll(HorizontalScrollView.FOCUS_LEFT)
        tvViewAllRecentTrans.setOnClickListener(this)
        /* Above horizontal menu clicks */
        llAddMoneyDashboard.setOnClickListener(this)
        llRequestMoneyDashboard.setOnClickListener(this)
        llAddBankDashboard.setOnClickListener(this)
        llSendMoneyDashboard.setOnClickListener(this)

        tvmobilerecharge.setOnClickListener(this)
        tvforex.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (appData!!.currency_symbol.isEmpty()) {
            FetchCountryCodesAsynTask().execute()
        }
        tvWalletAmtFrag.text = appData!!.currency_symbol + " " + appData!!.register_amount
    }

    override fun onClick(v: View?) {
        when (v) {
            llAddMoneyDashboard -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, AddMoneyEnterAmountFragment.CLASS_NAME)
                startActivity(intent)
            }
            llRequestMoneyDashboard -> {
                val intent = Intent(activity, PaymentActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.REQUEST_MONEY_TAB_POSITION)
                startActivity(intent)
            }
            llAddBankDashboard -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, AddBankAccountFragment.CLASS_NAME)
                startActivity(intent)
            }
            llSendMoneyDashboard -> {
                val intent = Intent(activity, PaymentActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.SEND_MONEY_TAB_POSITION)
                startActivity(intent)
            }
            tvViewAllRecentTrans -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, ViewAllRecentTransFragment.CLASS_NAME)
                startActivity(intent)
            }
            tvmobilerecharge -> {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, MobileRechargeFragment.CLASS_NAME)
                startActivity(intent)
            }
            tvforex -> {
                /*val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, GuulexFragment.CLASS_NAME)
                startActivity(intent)*/
                val intent = Intent(activity, GuulexfinalActivity::class.java)
                intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.GUULEX_BUY_TAB_POSITION)
                startActivity(intent)

            }
        }
    }


    private fun loadJSONFromAsset(): String? {
        var json: String? = ""
        try {
            val inputStream = context!!.getAssets().open(resources.getString(R.string.currenciesPath))
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            //showSomeErrorOccurredMsg(ex.toString())
        }

        return json
    }


    inner class FetchCountryCodesAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            val listType = object : TypeToken<ArrayList<CurrenciesModel>>() {}.type
            currenciesModellist = GsonBuilder().create().fromJson(loadJSONFromAsset(), listType)
            currenciesModellist =
                    return ""
        }

        override fun onPostExecute(result: String?) {
            if (currenciesModellist.size > 0) {
                for (i in currenciesModellist.indices) {
                    if (currenciesModellist.get(i).cc.equals(appData!!.register_currency_code)) {
                        appData!!.currency_symbol = currenciesModellist.get(i).symbol.toString()
                        tvWalletAmtFrag.text = appData!!.currency_symbol + " " + appData!!.register_amount
                        Log.d("Currency", appData!!.currency_symbol)
                        break
                    }
                }
            }
            loader.hide()
        }

    }
}