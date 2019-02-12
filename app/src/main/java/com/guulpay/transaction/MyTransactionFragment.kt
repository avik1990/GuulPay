package com.guulpay.transaction

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.customUiComponents.QuicksandRegularTextview
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulexfinal.CurrencyCodePresenter
import com.guulpay.guulexfinal.CurrencyListContract
import com.guulpay.guulexfinal.GuulexfinalActivity
import com.guulpay.guulexfinal.servicecall.CurrencyListRepositoryProvider
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.payments.send.paymentStatus.SendMoneyPaymentStatus
import com.guulpay.transaction.adapters.TransactionHistoryAdapter
import com.guulpay.transaction.servicecall.TransactionHistoryRepository
import com.guulpay.transaction.servicecall.TransactionHistoryRepositoryProvider
import kotlinx.android.synthetic.main.mytransaction_fragment.*


class MyTransactionFragment: BaseFragment(), TabLayout.OnTabSelectedListener, TransactionHistoryAdapter.TransactionHistoryListener, TransactionHistoryContract.View  {


    lateinit var transactionHistoryAdapter: TransactionHistoryAdapter
    private lateinit var transactionHistoryPresenter: TransactionHistoryPresenter
    var appData: AppData? = null
    companion object {
        const val CLASS_NAME = "MyTransactionFragment"
        fun newInstance(): Fragment {
            return MyTransactionFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.mytransaction_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        /* Transactions history list */
        appData = AppData(context as DashboardHomeActivity, Constants.Keys._KeyCryptoPreference)
        transactionHistoryPresenter = TransactionHistoryPresenter(this, appData!!, TransactionHistoryRepositoryProvider.getTranactionHistory(appData!!.remember_token))
        transactionHistoryAdapter = TransactionHistoryAdapter(this)
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwTransHistory.layoutManager = mLayoutManager
        recyclerVwTransHistory.itemAnimator = DefaultItemAnimator()
        recyclerVwTransHistory.adapter = transactionHistoryAdapter

        /* Adding Tabs */
        tabLayoutTransHistory.addTab(tabLayoutTransHistory.newTab())
        tabLayoutTransHistory.addTab(tabLayoutTransHistory.newTab())
        tabLayoutTransHistory.addTab(tabLayoutTransHistory.newTab())
        tabLayoutTransHistory.addTab(tabLayoutTransHistory.newTab())

        /* Custom view is inflated to make the text of Tab left-aligned */
        for (i in 0..tabLayoutTransHistory.tabCount-1){
            tabLayoutTransHistory.getTabAt(i)?.setCustomView(getCustomViewTab(i))
        }

        Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, TransactionStatusFragment.newInstance(),
                R.id.flTabContent, false, DateRangeFragment.CLASS_NAME)
        transactionHistoryPresenter?.callTranactionHistoryAPI(appData!!.user_id, 0, 10, "All","","")

    }

    private fun getCustomViewTab(pos:Int): View {
        val tabInflater = LayoutInflater.from(context).inflate(R.layout.custom_tabitems, null) as LinearLayout
        val tvTabContent = tabInflater.findViewById(R.id.tvTabContent) as QuicksandRegularTextview
        tvTabContent.text = resources.getStringArray(R.array.tabItems)[pos]
       // tvTabContent.setCustomFont(context, context?.getString(R.string.quicksandMedium),true)
        tabLayoutTransHistory.getTabAt(pos)?.setCustomView(tvTabContent)

        return tvTabContent
    }

    override fun initListeners() {
        tabLayoutTransHistory.addOnTabSelectedListener(this)

    }

    /* TransactionHistoryAdapter.TransactionHistoryListener */
    override fun onItemClick(pos: Int) {
        val intent = Intent(activity, HomeActivity::class.java)
        intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, SendMoneyPaymentStatus.CLASS_NAME)
        startActivity(intent)
    }

    /*  TabLayout.OnTabSelectedListener callback methods */
    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab?.position == 0){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, TransactionStatusFragment.newInstance(),
                    R.id.flTabContent, false, TransactionStatusFragment.CLASS_NAME)
        }
        else if (tab?.position == 1){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, TransactionTypeFragment.newInstance(),
                    R.id.flTabContent, false, TransactionTypeFragment.CLASS_NAME)
        }
        else if (tab?.position == 2){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, SpentOnFragment.newInstance(),
                    R.id.flTabContent, false, SpentOnFragment.CLASS_NAME)
        }
        else if (tab?.position == 3){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, DateRangeFragment.newInstance(),
                    R.id.flTabContent, false, DateRangeFragment.CLASS_NAME)
        }
    }



    override fun globalLogout() {
        //TODO: implement later
    }


    override fun setPresenter(presenter: TransactionHistoryContract.Presenter) {

            transactionHistoryPresenter = presenter as TransactionHistoryPresenter

    }

    override fun goToNextPage() {
        //TODO: implement later
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return isAdded
    }



    private val loader by lazy {
        LoaderDialog(context)
    }

    //Handler
    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }


    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        //TODO: implement later
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        //TODO: implement later
    }

}